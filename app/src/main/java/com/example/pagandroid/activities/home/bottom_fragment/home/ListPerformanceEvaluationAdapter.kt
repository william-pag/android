package com.example.pagandroid.activities.home.bottom_fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pagandroid.GetListPerformanceEvaluationQuery
import com.example.pagandroid.databinding.ItemPerformanceEvaluationBinding

class ListPerformanceEvaluationAdapter(
    private val context: Context,
    private val arrEvaluation: List<GetListPerformanceEvaluationQuery.GetListPerformanceEvaluation>
) : RecyclerView.Adapter<ListPerformanceEvaluationAdapter.ItemPerformanceEvaluation>() {
    inner class ItemPerformanceEvaluation(itemView: ItemPerformanceEvaluationBinding) :
        RecyclerView.ViewHolder(itemView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPerformanceEvaluation {
        val itemBinding: ItemPerformanceEvaluationBinding = ItemPerformanceEvaluationBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemPerformanceEvaluation(itemBinding)
    }

    override fun getItemCount(): Int {
        return arrEvaluation.size
    }

    override fun onBindViewHolder(holder: ItemPerformanceEvaluation, position: Int) {
        with(holder) {
            val evaluation = arrEvaluation[position]
            val binding = itemView as ItemPerformanceEvaluationBinding
            binding.userName.text = evaluation.user.name
            Glide.with(context).load(evaluation.user.image).into(binding.imgAvatar)
            if (evaluation.completedPercentage >= 66) {
                binding.btnFinalize.visibility = View.VISIBLE
            } else {
                val detail =
                    "${evaluation.completePerformance}/${evaluation.totalPerformance} - ${evaluation.completedPercentage}% Completed"
                binding.tvPercent.text = detail
                binding.tvPercent.visibility = View.VISIBLE
            }
        }
    }
}
