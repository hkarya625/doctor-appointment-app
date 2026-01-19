package com.arya.bookmydoc.domain.repository

import com.arya.bookmydoc.domain.model.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    suspend fun register(user: User,email:String, password: String): Result<Boolean>
    suspend fun login(email: String, password: String): Result<User>
    suspend fun logOut()
    suspend fun getUserData(userId: String): Result<User>
}