package com.example.pagandroid.service.overview

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.dao.Overview

class OverviewService {
    companion object {
        val shared = OverviewService()
    }

    suspend fun mapUserActions(page: Double = 1.0, name: Optional<String?> = Optional.Absent) {
        val result = Overview.shard.getListUserAction(page, name) ?: return
    }

    fun mapUsername(users: List<String>): String {
        return users.joinToString("\n")
    }
}