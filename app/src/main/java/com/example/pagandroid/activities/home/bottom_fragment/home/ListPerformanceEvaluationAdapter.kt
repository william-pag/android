package com.example.pagandroid.activities.home.bottom_fragment.home

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pagandroid.GetListPerformanceEvaluationQuery
import com.example.pagandroid.activities.home.bottom_fragment.adapter.RecycleViewAdapter
import com.example.pagandroid.databinding.ItemPerformanceEvaluationBinding
import kotlin.math.roundToInt

class ListPerformanceEvaluationAdapter(
    arrEvaluation: List<GetListPerformanceEvaluationQuery.GetListPerformanceEvaluation>,
    viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> ItemPerformanceEvaluationBinding
) : RecycleViewAdapter<
        GetListPerformanceEvaluationQuery.GetListPerformanceEvaluation,
        ItemPerformanceEvaluationBinding
        >(arrEvaluation, viewBindingFactory) {
    override fun bind(
        binding: ItemPerformanceEvaluationBinding,
        item: GetListPerformanceEvaluationQuery.GetListPerformanceEvaluation
    ) {
        val (_, completedPercentage, completePerformance, totalPerformance, user) = item
        binding.userName.text = user.name
        Glide.with(binding.root.context).load(user.image).into(binding.imgAvatar)

        if (completedPercentage > 66) {
            binding.btnFinalize.visibility = View.VISIBLE
        } else {
            @SuppressLint("DefaultLocale") val detail = String.format(
                "%d/%d - %d%s %s",
                completePerformance.roundToInt(),
                totalPerformance.roundToInt(),
                completedPercentage.roundToInt(),
                "%",
                "Completed"
            )
            val content = SpannableString(detail)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            binding.tvPercent.text = content
            binding.tvPercent.visibility = View.VISIBLE
        }
    }
}
