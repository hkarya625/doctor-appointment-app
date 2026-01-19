package com.arya.bookmydoc.domain.repository

import com.arya.bookmydoc.domain.model.User

interface RoomRepository {
    suspend fun getUser(id: String): User?
    suspend fun saveUser(user: User)
    suspend fun deleteUser(id: String)
}