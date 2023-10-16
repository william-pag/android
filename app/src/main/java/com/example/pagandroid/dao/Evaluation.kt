package com.example.pagandroid.dao

import com.example.pagandroid.GetLOCsAwaitingApprovalQuery
import com.example.pagandroid.dao.handle.HandleResult
import com.example.pagandroid.store.myApolloClient

class Evaluation {
    private val apolloClient = myApolloClient

    companion object {
        val shard = Evaluation()
    }

    suspend fun getLOCsAwaitingApproval(): GetLOCsAwaitingApprovalQuery.Data? {
        val result = this.apolloClient.client.query(GetLOCsAwaitingApprovalQuery()).execute()
        return HandleResult.shared.handleResult(result)
    }
}