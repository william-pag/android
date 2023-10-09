package com.example.pagandroid.room.dao

import androidx.room.*
import com.example.pagandroid.room.entities.UserLogin

@Dao
interface UserDao {
    @Insert
    fun insert(user: UserLogin)

    @Update
    fun update(user: UserLogin)

    @Delete
    fun delete(user: UserLogin)

    @Query("SELECT * FROM users")
    fun getAll(): List<UserLogin>

    @Query("SELECT * FROM users WHERE id = :id")
    fun findById(id: Int): UserLogin
}
