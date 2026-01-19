package com.arya.bookmydoc.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arya.bookmydoc.data.local.entity.UserEntity


@Dao
interface UserDao {

    @Query("SELECT * FROM user WHERE uid = :uid LIMIT 1")
    suspend fun getUseFlow(uid:String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM user WHERE uid = :id")
    suspend fun deleteUser(id: String)

}