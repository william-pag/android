package com.example.pagandroid.dao

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetAllDepartmentsQuery
import com.example.pagandroid.GetAllStrategiesQuery
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
}
