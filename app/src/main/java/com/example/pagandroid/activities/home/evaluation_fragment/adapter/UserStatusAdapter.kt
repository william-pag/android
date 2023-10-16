package com.example.pagandroid.activities.home.evaluation_fragment.adapter

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pagandroid.activities.adapter.RecycleViewAdapter
import com.example.pagandroid.databinding.ItemContributorStatusBinding
import com.example.pagandroid.model.LOCsWaitApproval.UserLOCsWaitApproval
import com.example.pagandroid.model.LOCsWaitApproval.UserWaitApproval

class UserStatusAdapter(
    users: List<UserLOCsWaitApproval>,
    viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> ItemContributorStatusBinding
) : RecycleViewAdapter<
        UserLOCsWaitApproval,
        ItemContributorStatusBinding
        >(users, viewBindingFactory) {
    override fun bind(binding: ItemContributorStatusBinding, item: UserLOCsWaitApproval) {
        binding.tvUsername.text = item.name
        if (item.deadline != null && item.status !="approved") {
            val content = SpannableString(item.deadline)
            content.setSpan(UnderlineSpan(), 0, content.length, 0)
            binding.tvOverdue.text = content
        } else {
            binding.tvOverdue.visibility = View.GONE
        }
        binding.tvStatus.text = item.status
        Glide.with(binding.root.context).load(item.image).into(binding.imageView)
    }
}