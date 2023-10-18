package com.example.pagandroid.service.overview

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetListUserActionQuery
import com.example.pagandroid.dao.Overview

class OverviewService {
    private val status = mapOf<String, String>(
        "open" to "In Progress",
        "approved" to "Approved",
        "complete" to "Completed",
        "not-started" to "Not Started",
        "pending-approval" to "Submitted",
        "in-progress" to "In Progress",
    )
    companion object {
        val shared = OverviewService()
    }

    suspend fun mapUserActions(name: String): GetListUserActionQuery.Data? {
        val result = Overview.shard.getUserAction(name)

        return result
    }

    fun mapUsername(users: List<String>): String {
        return users.joinToString("\n")
    }

    fun mapStatus(status: String?): String {
        return if (status == null) {
            "In Progress"
        } else {
            this.status[status] ?: "In Progress"
        }
    }
}