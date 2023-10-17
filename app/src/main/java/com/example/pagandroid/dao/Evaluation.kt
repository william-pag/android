package com.example.pagandroid.dao

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetAllEvaluationTypesQuery
import com.example.pagandroid.GetDistributionRatingsQuery
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

    suspend fun getAllEvaluationTypes(): GetAllEvaluationTypesQuery.Data? {
        val result = this.apolloClient.client.query(GetAllEvaluationTypesQuery()).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun getDistributionRatings(
        typeId: Optional<Double?> = Optional.Absent,
        questionId: Optional<Double?> = Optional.Absent,
    ): GetDistributionRatingsQuery.Data? {
        val result = this.apolloClient.client.query(GetDistributionRatingsQuery(typeId, questionId)).execute()
        return HandleResult.shared.handleResult(result)
    }
}