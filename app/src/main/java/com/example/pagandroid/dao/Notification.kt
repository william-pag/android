package com.example.pagandroid.dao

import com.example.pagandroid.ClearAllNotificationsMutation
import com.example.pagandroid.GetAllNotificationShortsQuery
import com.example.pagandroid.dao.handle.HandleResult
import com.example.pagandroid.store.myApolloClient

class Notification {
    private val apolloClient = myApolloClient
    companion object {
        val shard = Notification()
    }

    suspend fun getAllNotifications(): GetAllNotificationShortsQuery.Data? {
        val result = this.apolloClient.client.query(GetAllNotificationShortsQuery()).execute()
        return HandleResult.shared.handleResult(result)
    }
    suspend fun clearAllNotifications(): Boolean {
        val result = this
            .apolloClient
            .client
            .mutation(ClearAllNotificationsMutation())
            .execute()

        val status = result.data?.clearAllNotifications?.isNotEmpty() ?: false
        result.data?.clearAllNotifications?.toMutableList()?.clear()
        return status
    }
}