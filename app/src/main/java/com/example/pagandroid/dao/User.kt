package com.example.pagandroid.dao

import com.example.pagandroid.GetAllUserQuery
import com.example.pagandroid.LoginMutation
import com.example.pagandroid.MeQuery
import com.example.pagandroid.controllers.login.GlobalAction
import com.example.pagandroid.store.myApolloClient

class User() {
    private val apolloClient = myApolloClient
    companion object {
        val shard = User()
    }

    suspend fun getMe(): MeQuery.Data? {
        val result = this.apolloClient.client.query(MeQuery()).execute()
        return result.data
    }

    suspend fun getUser(): GetAllUserQuery.Data? {
        val result = this.apolloClient.client.query(GetAllUserQuery()).execute()
        return result.data
    }

    suspend fun login(email: String, password: String): String? {
        val result = this.apolloClient.client.mutation(LoginMutation(email = email, password = password)).execute()
        if (result.data != null) {
            this.apolloClient.setClient(result.data?.login?.accessToken.toString())
            return result.data?.login?.accessToken.toString()
        } else {
            result.errors?.first()?.message?.let { GlobalAction.shared.makeToast(it) }
            return null
        }
    }
}
