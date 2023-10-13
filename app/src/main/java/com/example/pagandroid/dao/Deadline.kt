package com.example.pagandroid.dao

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetAllDeadlineQuery
import com.example.pagandroid.GetAllReminderQuery
import com.example.pagandroid.dao.handle.HandleResult
import com.example.pagandroid.store.myApolloClient

class Deadline {
    private val apolloClient = myApolloClient
    companion object {
        val shard = Deadline()
    }

    suspend fun getAllDeadlines(
        strategyId: Optional<Double?> = Optional.Absent,
        departmentId: Optional<Double?> = Optional.Absent,
    ): GetAllDeadlineQuery.Data? {
        val result = this.apolloClient.client.query(GetAllDeadlineQuery(departmentId, strategyId)).execute()
        return HandleResult.shared.handleResult(result)
    }
}