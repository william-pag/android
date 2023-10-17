package com.example.pagandroid.service.LOCsWaitApproval

import com.example.pagandroid.dao.Evaluation
import com.example.pagandroid.helpers.Datetime
import com.example.pagandroid.model.LOCsWaitApproval.EvaluationTypeAndQuestion
import com.example.pagandroid.model.LOCsWaitApproval.Header
import com.example.pagandroid.model.LOCsWaitApproval.ILOCsWaitApproval
import com.example.pagandroid.model.LOCsWaitApproval.Percentage
import com.example.pagandroid.model.LOCsWaitApproval.UserLOCsWaitApproval
import com.example.pagandroid.model.LOCsWaitApproval.UserWaitApproval
import com.example.pagandroid.service.evaluation.EvaluationTypeService

class LOCsWaitApprovalService {
    companion object {
        val shared = LOCsWaitApprovalService()
    }

    suspend fun mapLOCsWaitApproval(): List<ILOCsWaitApproval> {
        val listLOC = mutableListOf<ILOCsWaitApproval>()
        val result = Evaluation.shard.getLOCsAwaitingApproval()
        if (result?.getLOCsAwaitingApproval != null){
            val data = result.getLOCsAwaitingApproval

            listLOC.add(Header(title = "List Of Contributors Awaiting Approval"))
            listLOC.add(Percentage(
                complete = data.complete.toInt(),
                total = data.total.toInt(),
                percent = data.percentComplete.toInt()
            ))
            val users = mutableListOf<UserLOCsWaitApproval>()
            val mapStatus = mapOf<String, String>(
                "approved" to "Approved",
                "not-started" to "Not Started",
                "open" to "In Progress"
            )
            data.users.forEach { user ->
                users.add(UserLOCsWaitApproval(
                    id = user.id.toString(),
                    name = user.name,
                    image = user.image ?: "",
                    status = mapStatus[user.cycleContributors[0].status] ?: "",
                    deadline = if (user.department != null && (user.department.deadlineConfirmLOC as String) != "") {
                        "${Datetime.shard.countDateToNow(user.department.deadlineConfirmLOC)} Days Overdue"
                    } else {
                        null
                    }
                ))
            }
            listLOC.add(UserWaitApproval(users))
        }
        listLOC.add(Header(title = "Distribution of your ratings on evaluates for this cycle"))
        listLOC.add(EvaluationTypeAndQuestion(data = EvaluationTypeService.shared.mapEvaluationType()))
        return listLOC
    }
}