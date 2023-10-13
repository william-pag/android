package com.example.pagandroid.service.deadline

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.dao.Deadline
import com.example.pagandroid.helpers.Datetime
import com.example.pagandroid.model.deadline.DeadlineModel
import com.example.pagandroid.model.deadline.DeadlineTypes

class DeadlineService {
    companion object {
        val shared = DeadlineService()
    }
    suspend fun getListDeadline(
        strategyId: Optional<Double?> = Optional.Absent,
        departmentId: Optional<Double?> = Optional.Absent,
    ): List<DeadlineModel> {
        val deadlines = Deadline.shard.getAllDeadlines(strategyId, departmentId)
        val deadlinesModel = mutableListOf<DeadlineModel>();
        if (deadlines?.getAllDepartments != null) {
            deadlines.getAllDepartments.forEach { deadline ->
                val deadlineLOC = DeadlineModel(
                    id = deadline.id,
                    name = "${deadline.strategy?.name} - ${deadline.name}",
                    deadlineType = DeadlineTypes.deadlineLOC,
                    datetime = if (deadline.deadlineLOC != null) {
                        Datetime.shard.formatDateTime(deadline.deadlineLOC as String, "MMM-dd-yyyy")
                    } else {
                        "Invalid Date"
                    },
                    overdue = if (deadline.deadlineLOC != null) {
                        val day = Datetime.shard.countDateToNow(deadline.deadlineLOC as String)
                        "$day Days Overdue"
                    } else {
                        "Overdue"
                    }
                )
                deadlinesModel.add(deadlineLOC)

                val deadlineConfirmLOC = DeadlineModel(
                    id = deadline.id,
                    name = "${deadline.strategy?.name} - ${deadline.name}",
                    deadlineType = DeadlineTypes.deadlineConfirmLOC,
                    datetime = if (deadline.deadlineConfirmLOC != null) {
                        Datetime.shard.formatDateTime(deadline.deadlineConfirmLOC as String, "MMM-dd-yyyy")
                    } else {
                        "Invalid Date"
                    },
                    overdue = if (deadline.deadlineConfirmLOC != null) {
                        val day = Datetime.shard.countDateToNow(deadline.deadlineConfirmLOC as String)
                        "$day Days Overdue"
                    } else {
                        "Overdue"
                    }
                )
                deadlinesModel.add(deadlineConfirmLOC)

                val deadlineSelfAssessment = DeadlineModel(
                    id = deadline.id,
                    name = "${deadline.strategy?.name} - ${deadline.name}",
                    deadlineType = DeadlineTypes.deadlineSelfAssessment,
                    datetime = if (deadline.deadlineSelfAssessment != null) {
                        Datetime.shard.formatDateTime(deadline.deadlineSelfAssessment as String, "MMM-dd-yyyy")
                    } else {
                        "Invalid Date"
                    },
                    overdue = if (deadline.deadlineSelfAssessment != null) {
                        val day = Datetime.shard.countDateToNow(deadline.deadlineSelfAssessment as String)
                        "$day Days Overdue"
                    } else {
                        "Overdue"
                    }
                )
                deadlinesModel.add(deadlineSelfAssessment)

                val deadlinePerformanceEvaluation = DeadlineModel(
                    id = deadline.id,
                    name = "${deadline.strategy?.name} - ${deadline.name}",
                    deadlineType = DeadlineTypes.deadlinePerformanceEvaluation,
                    datetime = if (deadline.deadlinePerformanceEvaluation != null) {
                        Datetime.shard.formatDateTime(deadline.deadlinePerformanceEvaluation as String, "MMM-dd-yyyy")
                    } else {
                        "Invalid Date"
                    },
                    overdue = if (deadline.deadlinePerformanceEvaluation != null) {
                        val day = Datetime.shard.countDateToNow(deadline.deadlinePerformanceEvaluation as String)
                        "$day Days Overdue"
                    } else {
                        "Overdue"
                    }
                )
                deadlinesModel.add(deadlinePerformanceEvaluation)
            }
        }
        return deadlinesModel
    }
}