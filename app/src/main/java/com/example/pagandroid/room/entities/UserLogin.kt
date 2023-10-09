package com.example.pagandroid.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
class UserLogin (
    @PrimaryKey(autoGenerate = true) var id: Int,
    val name: String,
    val email: String,
    val cycleId: Double?,
    val image: String?
) {
    override fun toString(): String {
        return "$id $name $email $cycleId $image"
    }
}
