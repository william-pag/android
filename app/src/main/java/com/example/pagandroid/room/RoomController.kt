package com.example.pagandroid.room

import android.content.Context
import androidx.room.Room
import com.example.pagandroid.room.dao.AppDatabase
import com.example.pagandroid.room.entities.UserLogin

data class DatabaseName(val id: Int = 0) {
    companion object {
        val userLogin: String = "user_login"
    }
}

class RoomController(private val context: Context) {
    private val id: Int = 1000
    private val builder = Room.databaseBuilder(context, AppDatabase::class.java, DatabaseName.userLogin).build()

    fun getMe(): UserLogin {
        val user = builder.userDao().findById(this.id);

        return user
    }

    fun addUserLogin(user: UserLogin) {
        user.id = this.id
        builder.userDao().insert(user = user)
    }
    fun deleteUser(user: UserLogin) {
        user.id = this.id
        builder.userDao().delete(user)
    }
}
