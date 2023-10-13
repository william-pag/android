package com.example.pagandroid.activities.home.bottom_fragment.dropdown

import com.example.pagandroid.GetAllUserNameQuery
import com.example.pagandroid.dao.User

interface IGetUser {
    suspend fun getAllUsers(): MutableList<GetAllUserNameQuery.GetAllUser>? {
        val result = User.shard.getUser()
        val arrUsers = result?.getAllUsers?.toMutableList()
        arrUsers?.add(0, GetAllUserNameQuery.GetAllUser(0.0, "All", "", null))
        return arrUsers
    }
}