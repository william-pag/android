package com.example.pagandroid.activities.home.bottom_fragment.home

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pagandroid.GetListPerformanceEvaluationQuery
import com.example.pagandroid.databinding.ItemPerformanceEvaluationBinding
import kotlin.math.roundToInt

class ListPerformanceEvaluationAdapter(
    private val arrEvaluation: List<GetListPerformanceEvaluationQuery.GetListPerformanceEvaluation>
) : RecyclerView.Adapter<ListPerformanceEvaluationAdapter.ItemPerformanceEvaluation>() {
    inner class ItemPerformanceEvaluation(val itemBinding: ItemPerformanceEvaluationBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPerformanceEvaluation {
        return ItemPerformanceEvaluation(ItemPerformanceEvaluationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun getItemCount(): Int {
        return arrEvaluation.size
    }

    override fun onBindViewHolder(holder: ItemPerformanceEvaluation, position: Int) {
        holder.itemBinding
        val (_, completedPercentage, completePerformance, totalPerformance, user) = arrEvaluation[position]
        val context = holder.itemView.context
        val binding = holder.itemBinding
        binding.userName.text = user.name
        Glide.with(context).load(user.image).into(binding.imgAvatar)

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
