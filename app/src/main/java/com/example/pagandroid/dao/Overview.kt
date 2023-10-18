package com.example.pagandroid.dao

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.*
import com.example.pagandroid.controllers.login.GlobalAction
import com.example.pagandroid.dao.handle.HandleResult
import com.example.pagandroid.store.myApolloClient

class Overview {
    private val apolloClient = myApolloClient

    companion object {
        val shard = Overview()
    }

    suspend fun getAllDepartments(strategyId: Optional<Double?> = Optional.Absent): GetAllDepartmentsQuery.Data? {
        val result = this.apolloClient.client.query(GetAllDepartmentsQuery(strategyId = strategyId)).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun getAllStrategies(): GetAllStrategiesQuery.Data? {
        val result = this.apolloClient.client.query(GetAllStrategiesQuery()).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun getOverallProgress(
        departmentIds: Optional<List<Double>?> = Optional.Absent,
        strategyId: Optional<Double?> = Optional.Absent
    ): OverallProgressQuery.Data? {
        val result = this.apolloClient.client.query(OverallProgressQuery(departmentIds, strategyId)).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun getPerformanceEvaluation(
        departmentIds: Optional<List<Double>?> = Optional.Absent,
        strategyId: Optional<Double?> = Optional.Absent
    ): PerformanceEvaluationQuery.Data? {
        val result = this.apolloClient.client.query(PerformanceEvaluationQuery(strategyId, departmentIds)).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun getListContributor(
        departmentIds: Optional<List<Double>?> = Optional.Absent,
        strategyId: Optional<Double?> = Optional.Absent
    ): ListContributorQuery.Data? {
        val result = this.apolloClient.client.query(ListContributorQuery(strategyId, departmentIds)).execute()
        return HandleResult.shared.handleResult(result)
    }
    suspend fun getSelfAssessment(
        departmentIds: Optional<List<Double>?> = Optional.Absent,
        strategyId: Optional<Double?> = Optional.Absent
    ): SelfAssessmentQuery.Data? {
        val result = this.apolloClient.client.query(SelfAssessmentQuery(strategyId, departmentIds)).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun getListPerformanceEvaluations(): GetListPerformanceEvaluationQuery.Data? {
        val result = this.apolloClient.client.query(GetListPerformanceEvaluationQuery()).execute()
        return HandleResult.shared.handleResult(result)
    }

    suspend fun getListUserAction(page: Double = 1.0, name: Optional<String?> = Optional.Absent): GetListUserActionQuery.Data? {
        val result = this.apolloClient.client.query(GetListUserActionQuery(page, name)).execute()
        return HandleResult.shared.handleResult(result)
    }
}
