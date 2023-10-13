package com.example.pagandroid.activities.home.bottom_fragment.deadline

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.pagandroid.activities.home.bottom_fragment.adapter.RecycleViewAdapter
import com.example.pagandroid.databinding.ItemDeadlineBinding
import com.example.pagandroid.model.deadline.DeadlineModel

class ListDeadlineAdapter(
    listDeadline: List<DeadlineModel>,
    viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> ItemDeadlineBinding
): RecycleViewAdapter<
        DeadlineModel,
        ItemDeadlineBinding
        >(listDeadline, viewBindingFactory) {
    override fun bind(
        binding: ItemDeadlineBinding,
        item: DeadlineModel
    ) {
        binding.deadlineType.text = item.deadlineType
        binding.tvStrategy.text = item.name
        binding.tvDeadlineDate.text = item.datetime
        binding.tvOverdue.text = item.overdue
    }
}