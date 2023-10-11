package com.example.pagandroid.dao.handle

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.controllers.login.GlobalAction

class HandleResult {
    companion object {
        val shared = HandleResult()
    }
    fun <D: Operation.Data> handleResult(result: ApolloResponse<D>): D? {
        return if (result.data == null) {
            GlobalAction.shared.redirectLogin()
            result.errors?.first()?.let { GlobalAction.shared.makeToast(it.message) }
            null
        } else {
            result.data
        }
    }
}
