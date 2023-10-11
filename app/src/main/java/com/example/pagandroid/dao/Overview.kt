package com.example.pagandroid.dao

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetAllDepartmentsQuery
import com.example.pagandroid.GetAllStrategiesQuery
import com.example.pagandroid.ListContributorQuery
import com.example.pagandroid.OverallProgressQuery
import com.example.pagandroid.PerformanceEvaluationQuery
import com.example.pagandroid.SelfAssessmentQuery
import com.example.pagandroid.controllers.login.GlobalAction
import com.example.pagandroid.store.myApolloClient

class Overview {
    private val apolloClient = myApolloClient

    companion object {
        val shard = Overview()
    }

    suspend fun getAllDepartments(strategyId: Optional<Double?> = Optional.Absent): GetAllDepartmentsQuery.Data? {
        val result = this.apolloClient.client.query(GetAllDepartmentsQuery(strategyId = strategyId)).execute()
        return if (result.data == null) {
            GlobalAction.shared.redirectLogin()
            result.errors?.first()?.let { GlobalAction.shared.makeToast(it.message) }
            null
        } else {
            result.data
        }
    }

    suspend fun getAllStrategies(): GetAllStrategiesQuery.Data? {
        val result = this.apolloClient.client.query(GetAllStrategiesQuery()).execute()
        return if (result.data == null) {
            GlobalAction.shared.redirectLogin()
            result.errors?.first()?.let { GlobalAction.shared.makeToast(it.message) }
            null
        } else {
            result.data
        }
    }

    suspend fun getOverallProgress(
        departmentIds: Optional<List<Double>?> = Optional.Absent,
        strategyId: Optional<Double?> = Optional.Absent
    ): OverallProgressQuery.Data? {
        val result = this.apolloClient.client.query(OverallProgressQuery(departmentIds, strategyId)).execute()
        return if (result.data == null) {
            GlobalAction.shared.redirectLogin()
            result.errors?.first()?.let { GlobalAction.shared.makeToast(it.message) }
            null
        } else {
            result.data
        }
    }

    suspend fun getPerformanceEvaluation(
        departmentIds: Optional<List<Double>?> = Optional.Absent,
        strategyId: Optional<Double?> = Optional.Absent
    ): PerformanceEvaluationQuery.Data? {
        val result = this.apolloClient.client.query(PerformanceEvaluationQuery(strategyId, departmentIds)).execute()
        return if (result.data == null) {
            GlobalAction.shared.redirectLogin()
            result.errors?.first()?.let { GlobalAction.shared.makeToast(it.message) }
            null
        } else {
            result.data
        }
    }

    suspend fun getListContributor(
        departmentIds: Optional<List<Double>?> = Optional.Absent,
        strategyId: Optional<Double?> = Optional.Absent
    ): ListContributorQuery.Data? {
        val result = this.apolloClient.client.query(ListContributorQuery(strategyId, departmentIds)).execute()
        return if (result.data == null) {
            GlobalAction.shared.redirectLogin()
            result.errors?.first()?.let { GlobalAction.shared.makeToast(it.message) }
            null
        } else {
            result.data
        }
    }
    suspend fun getSelfAssessment(
        departmentIds: Optional<List<Double>?> = Optional.Absent,
        strategyId: Optional<Double?> = Optional.Absent
    ): SelfAssessmentQuery.Data? {
        val result = this.apolloClient.client.query(SelfAssessmentQuery(strategyId, departmentIds)).execute()
        return if (result.data == null) {
            GlobalAction.shared.redirectLogin()
            result.errors?.first()?.let { GlobalAction.shared.makeToast(it.message) }
            null
        } else {
            result.data
        }
    }
}
