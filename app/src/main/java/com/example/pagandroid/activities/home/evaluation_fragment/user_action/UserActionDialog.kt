package com.example.pagandroid.activities.home.evaluation_fragment.user_action

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.pagandroid.dao.Overview
import com.example.pagandroid.databinding.UserActionDialogBinding
import com.example.pagandroid.helpers.Datetime
import com.example.pagandroid.service.overview.OverviewService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserActionDialog(private val context: Context, private val name: String): Dialog(context) {
    private lateinit var binding: UserActionDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserActionDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.IO).launch {
            createUserActionDialog(name)
        }
    }

    @SuppressLint("SetTextI18n")
    private suspend fun createUserActionDialog(userName: String) {
        val dataUserAction = Overview.shard.getUserAction(userName)
        val (
            user,
            listOfContributors,
            selfAssessment,
            evaluationsFor
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
            binding.tvEvaluationFor.text = "${evaluationsFor?.percentComplete?.times(100)?.toInt() ?: 0}%\n${
                if (evaluationsFor?.evaluations != null) {
                    OverviewService.shared.mapUsername(evaluationsFor.evaluations.map { item -> item.contributor?.name ?: "" })
                } else {
                    ""
                }
            }"
        }
    }
}