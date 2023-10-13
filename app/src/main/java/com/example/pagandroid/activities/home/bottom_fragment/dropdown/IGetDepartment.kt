package com.example.pagandroid.activities.home.bottom_fragment.dropdown

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetAllDepartmentsQuery
import com.example.pagandroid.dao.Overview

interface IGetDepartment {
    suspend fun getAllDepartments(strategyId: Optional<Double?> = Optional.Absent): MutableList<GetAllDepartmentsQuery.GetAllDepartment>? {
        val departments =  Overview.shard.getAllDepartments(strategyId = strategyId)
        var arrDepartments = departments?.getAllDepartments?.toMutableList()
        if (strategyId == Optional.Absent) {
            val mapDepartment = mutableMapOf<String, GetAllDepartmentsQuery.GetAllDepartment>()
            arrDepartments?.forEach { getAllDepartment ->
                mapDepartment[getAllDepartment.name] = getAllDepartment
            }
            arrDepartments = mapDepartment.values.toMutableList()
            mapDepartment.clear()
        }
        arrDepartments?.add(0, GetAllDepartmentsQuery.GetAllDepartment(0.0, "Select Department", null))
        return arrDepartments
    }
}