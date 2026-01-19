package com.arya.bookmydoc.data.remote

import com.arya.bookmydoc.data.dto.AppointmentDto
import com.arya.bookmydoc.data.dto.CategoryDto
import com.arya.bookmydoc.data.dto.DoctorDto
import com.arya.bookmydoc.data.dto.UserDto
import com.arya.bookmydoc.data.mapper.toDomain
import com.arya.bookmydoc.data.mapper.toDto
import com.arya.bookmydoc.domain.model.Appointment
import com.arya.bookmydoc.domain.model.AppointmentStatus
import com.arya.bookmydoc.domain.model.Category
import com.arya.bookmydoc.domain.model.Doctor
import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.model.UserRole
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

interface FirebaseDataSource {

    suspend fun register(user: User, email:String, password: String): Result<Boolean>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logOut()
    suspend fun getUserData(userId: String): Result<User>

    suspend fun fetchDoctor(): Result<List<Doctor>>
    suspend fun fetchCategories():Result<List<Category>>

    suspend fun getAppointments(appointmentIds: List<String>): Result<List<Appointment>>
    suspend fun bookAppointment(appointment: Appointment): Result<Boolean>
}

class FirebaseDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
): FirebaseDataSource{
    override suspend fun register(
        user: User,
        email: String,
        password: String
    ): Result<Boolean> =withContext(Dispatchers.IO) {
        runCatching {

            // Validate password
            if (password.length < 6) {
                throw IllegalArgumentException("Password must be at least 6 characters")
            }

            // Create user in Firebase Auth
            val authResult = firebaseAuth
                .createUserWithEmailAndPassword(email.trim(), password)
                .await()

            val firebaseUser = authResult.user
                ?: throw IllegalStateException("User created but user object is null")

            // Prepare Firestore data
            val userDto = user.copy(id = firebaseUser.uid).toDto()

            // Save user in Firestore
            firestore.collection("users")
                .document(firebaseUser.uid)
                .set(userDto)
                .await()

            // SUCCESS → return true
            true
        }.recoverCatching { exception ->
            when (exception) {
                is FirebaseAuthWeakPasswordException ->
                    throw IllegalArgumentException("Password is too weak")

                is FirebaseAuthInvalidCredentialsException ->
                    throw IllegalArgumentException("Invalid email format")

                is FirebaseAuthUserCollisionException ->
                    throw IllegalStateException("An account already exists with this email")

                else -> throw exception
            }
        }
    }

    override suspend fun login(email: String, password: String): Result<User> =
        withContext(Dispatchers.IO){
            try{
                val result = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).await()
                val firebaseUser = result.user
                    ?: throw IllegalStateException("Login succeeded but FirebaseUser is null")

                val user = getUserData(firebaseUser.uid)
                user
            }catch (exception: Exception) {
                when(exception){
                    is FirebaseAuthInvalidUserException -> Result.failure(Exception("No account found with this email"))
                    is FirebaseAuthInvalidCredentialsException -> Result.failure(Exception("Incorrect password"))
                    else -> Result.failure(exception)
                }
            }
        }
    override suspend fun logOut() = firebaseAuth.signOut()


    override suspend fun getUserData(userId: String): Result<User> =
        withContext(Dispatchers.IO) {
            try {
                val dto = firestore
                    .collection("users")
                    .document(userId)
                    .get()
                    .await()
                    .toObject(UserDto::class.java)
                    ?: return@withContext Result.failure(
                        IllegalStateException("User data not found")
                    )

                Result.success(dto.toDomain())

            } catch (e: Exception) {
                Result.failure(e)
            }
        }


    override suspend fun fetchDoctor(): Result<List<Doctor>> = withContext(Dispatchers.IO){
        try {
            val snapshot = firestore
                .collection("doctors")
                .get()
                .await()

            val doctors = snapshot.toObjects(DoctorDto::class.java)
                ?.map { it.toDomain() }
                ?: emptyList()

            Result.success(doctors)

        } catch (e: Exception) {
            when (e) {
                is FirebaseFirestoreException -> Result.failure(
                    Exception("Permission Denied: ${e.message}")
                )
                else -> Result.failure(
                    Exception("Unexpected error: ${e.message}")
                )
            }
        }
    }

    override suspend fun fetchCategories(): Result<List<Category>> = withContext(Dispatchers.IO){
        try {
            val snapshot = firestore
                .collection("categories")
                .get()
                .await()

            val categories = snapshot.toObjects(CategoryDto::class.java)
                ?.map { it.toDomain() }
                ?:emptyList()
            Result.success(categories)
        }catch (e: Exception){
            when(e){
                is FirebaseFirestoreException -> Result.failure(
                    Exception("Permission Denied: ${e.message}")
                )
                else -> Result.failure(
                    Exception("Unexpected error: ${e.message}")
                )
            }
        }
    }

    override suspend fun getAppointments(appointmentIds: List<String>): Result<List<Appointment>> = withContext(Dispatchers.IO){
        try {
            coroutineScope {
                val appointments = appointmentIds.map { appointmentId->
                    async {
                        firestore
                            .collection("appointments")
                            .document(appointmentId)
                            .get()
                            .await()
                            .toObject(AppointmentDto::class.java)
                            ?.toDomain()
                    }
                }
                Result.success(appointments.awaitAll().filterNotNull())
            }
        }catch (e: Exception){
            when(e){
                is FirebaseFirestoreException -> Result.failure(
                    Exception("Permission Denied: ${e.message}")
                )
                else -> Result.failure(
                    Exception("Unexpected error: ${e.message}")
                )
            }
        }
    }

    override suspend fun bookAppointment(appointment: Appointment): Result<Boolean> =
        withContext(Dispatchers.IO) {
            try {
                val userId = firebaseAuth.currentUser?.uid
                    ?: return@withContext Result.failure(Exception("User not logged in"))

                val appointmentsRef = firestore.collection("appointments")

                val result = firestore.runTransaction { transaction ->
                    val newAppointmentRef = appointmentsRef.document()

                    val appointmentDto = appointment.copy(
                        id = newAppointmentRef.id,
                        patientId = userId,
                        status = AppointmentStatus.CONFIRMED
                    ).toDto()

                    // Save appointment
                    transaction.set(newAppointmentRef, appointmentDto)

                    // Update doctor document
                    val doctorRef = firestore.collection("doctors")
                        .document(appointment.doctorId)
                    transaction.update(
                        doctorRef,
                        "appointments",
                        FieldValue.arrayUnion(newAppointmentRef.id)
                    )

                    // Update user document
                    val userRef = firestore.collection("users")
                        .document(userId)
                    transaction.update(
                        userRef,
                        "appointments",
                        FieldValue.arrayUnion(newAppointmentRef.id)
                    )

                    true // value returned by transaction
                }.await()

                Result.success(result)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
}