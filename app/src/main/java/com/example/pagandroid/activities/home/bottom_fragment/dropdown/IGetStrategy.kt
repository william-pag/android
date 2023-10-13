package com.example.pagandroid.activities.home.bottom_fragment.dropdown

import com.example.pagandroid.GetAllStrategiesQuery
import com.example.pagandroid.dao.Overview

interface IGetStrategy {
    suspend fun getAllStrategies(): MutableList<GetAllStrategiesQuery.GetAllStrategy>? {
        val strategies =  Overview.shard.getAllStrategies()
        val arrStrategy = strategies?.getAllStrategies?.toMutableList()
        arrStrategy?.add(0, GetAllStrategiesQuery.GetAllStrategy(0.0, "Select Strategy"))
        return arrStrategy
    }
}