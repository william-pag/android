package com.example.pagandroid.dao

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetAllReminderQuery
import com.example.pagandroid.GetAllUserNameQuery
import com.example.pagandroid.LoginMutation
import com.example.pagandroid.MeQuery
import com.example.pagandroid.controllers.login.GlobalAction
import com.example.pagandroid.dao.handle.HandleResult
import com.example.pagandroid.store.myApolloClient

class Reminder {
    private val apolloClient = myApolloClient
    companion object {
        val shard = Reminder()
    }

    suspend fun getAllReminders(userId: Optional<Double?> = Optional.Absent): GetAllReminderQuery.Data? {
        val result = this.apolloClient.client.query(GetAllReminderQuery(userId)).execute()
        return HandleResult.shared.handleResult(result)
    }
}
