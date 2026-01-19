package com.arya.bookmydoc.data.repository

import com.arya.bookmydoc.data.remote.FirebaseDataSource
import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firebaseDataSource: FirebaseDataSource
): UserRepository {
    override suspend fun register(
        user: User,
        email: String,
        password: String
    ): Result<Boolean> {
        return firebaseDataSource.register(user, email, password)
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<User> {
        return firebaseDataSource.login(email, password)
    }

    override suspend fun logOut() {
        firebaseDataSource.logOut()
    }


    override suspend fun getUserData(userId:String): Result<User> {
        return firebaseDataSource.getUserData(userId)
    }
}