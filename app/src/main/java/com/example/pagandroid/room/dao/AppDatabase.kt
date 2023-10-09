package com.example.pagandroid.room.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.pagandroid.room.entities.UserLogin

@Database(entities = [UserLogin::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
