package com.example.pagandroid.dao

import com.example.pagandroid.*
import com.example.pagandroid.controllers.login.GlobalAction
import com.example.pagandroid.dao.handle.HandleResult
import com.example.pagandroid.store.myApolloClient

class User() {
    private val apolloClient = myApolloClient
    companion object {
        val shard = User()
    }

    suspend fun getMe(): MeQuery.Data? {
        val result = this.apolloClient.client.query(MeQuery()).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun getUser(): GetAllUserNameQuery.Data? {
        val result = this.apolloClient.client.query(GetAllUserNameQuery()).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun login(email: String, password: String): String? {
        val result = this.apolloClient.client.mutation(LoginMutation(email = email, password = password)).execute()
        return if (result.data != null) {
            this.apolloClient.setClient(result.data?.login?.accessToken.toString())
            result.data?.login?.accessToken.toString()
        } else {
            result.errors?.first()?.message?.let { GlobalAction.shared.makeToast(it) }
            null
        }
    }
    suspend fun getDetailUser(userId: Double): GetOneUserQuery.Data? {
        val result = this.apolloClient.client.query(GetOneUserQuery(userId)).execute()
        return HandleResult.shared.handleResult(result)
    }
}
