package com.example.pagandroid.activities.home.evaluation_fragment.user_action

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.pagandroid.dao.Overview
import com.example.pagandroid.databinding.UserActionDialogBinding
import com.example.pagandroid.helpers.Datetime
import com.example.pagandroid.service.overview.OverviewService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserActionDialog(
    private val context: Context,
    private val name: String,
): Dialog(context) {
    private lateinit var binding: UserActionDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserActionDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.IO).launch {
            createUserActionDialog(name)
        }
        binding.imgClose.setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun createUserActionDialog(userName: String) {
        val dataUserAction = Overview.shard.getUserAction(userName)
        val (
            user,
            listOfContributors,
            selfAssessment,
            evaluationsFor,
            evaluationsBy,
            performanceSummary,
            overallPerformanceSummary,
        ) = dataUserAction
            ?.getListUserAction
            ?.data
            ?.get(0) ?: return
        CoroutineScope(Dispatchers.Main).launch {
            Glide.with(context).load(user.image).into(binding.imgAvatar)
            binding.tvUsername.text = user.name
            binding.tvTitle.text = user.title?.name
            binding.tvStartDate.text = Datetime.shard.formatDateTime(user.startDate.toString())
            binding.tvLocation.text = user.location?.name
            binding.tvDepartment.text = user.department?.name
            binding.tvEvaluator.text = user.evaluator?.name
            binding.tvForm.text = user.evaluationType?.name
            binding.tvLoc.text = OverviewService.shared.mapStatus(listOfContributors?.get(0)?.status)
            binding.tvSelfAssessment.text = OverviewService.shared.mapStatus(selfAssessment?.status)
            binding.tvEvaluationFor.text = "${
                OverviewService.shared.mapDoubleTwoDigit(evaluationsFor?.percentComplete?.times(100))
            }%\n${
                if (evaluationsFor?.evaluations != null) {
                    OverviewService.shared.mapUsername(evaluationsFor.evaluations.map { item -> item.contributor?.name ?: "" })
                } else {
                    ""
                }
            }"
            binding.tvEvaluationBy.text = "${
                OverviewService.shared.mapDoubleTwoDigit(evaluationsBy?.percentComplete?.times(100))
            }%\n${
                if (evaluationsBy?.evaluations != null) {
                    OverviewService.shared.mapUsername(evaluationsBy.evaluations.map { item -> item.evaluatee?.name ?: "" })
                } else {
                    ""
                }
            }"
            var isCompleted = true
            isCompleted = if (performanceSummary?.get(0) != null) {
                performanceSummary[0].isComplete == true
            } else {
                false
            }
            if (isCompleted) {
                binding.tvPerfSummary.text = "Yes"
            } else {
                binding.tvPerfSummary.setTextColor(Color.RED)
                binding.tvPerfSummary.text = "No"
            }
            var isShared = true
            isShared = if (overallPerformanceSummary?.get(0) != null) {
                overallPerformanceSummary[0].isShare == true
            } else {
                false
            }
            if (isShared) {
                binding.tvOverallPerfSummary.text = "Shared\n${
                    if (overallPerformanceSummary?.get(0) != null) {
                        Datetime.shard.formatDateTime(overallPerformanceSummary[0].sharedDate.toString())
                    } else {
                        ""
                    }
                }"
            } else {
                binding.tvOverallPerfSummary.setTextColor(Color.RED)
                binding.tvOverallPerfSummary.text = "N/A"
            }
        }
    }
}