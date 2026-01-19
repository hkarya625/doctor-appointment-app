package com.arya.bookmydoc.data.repository

import android.util.Log
import com.arya.bookmydoc.data.local.dao.UserDao
import com.arya.bookmydoc.data.mapper.toDomain
import com.arya.bookmydoc.data.mapper.toEntity
import com.arya.bookmydoc.domain.model.User
import com.arya.bookmydoc.domain.repository.RoomRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class RoomRepositoryImpl@Inject constructor(
    private val userDao: UserDao
): RoomRepository {
    override suspend fun getUser(id: String): User? {
        return userDao.getUseFlow(id)?.toDomain()
    }

    override suspend fun saveUser(user: User) {
        userDao.insertUser(user.toEntity())
    }

    override suspend fun deleteUser(id: String) {
        userDao.deleteUser(id)
    }
}