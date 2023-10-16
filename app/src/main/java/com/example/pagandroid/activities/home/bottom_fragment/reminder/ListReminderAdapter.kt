package com.example.pagandroid.activities.home.bottom_fragment.reminder

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import com.example.pagandroid.GetAllReminderQuery
import com.example.pagandroid.activities.adapter.RecycleViewAdapter
import com.example.pagandroid.databinding.ItemReminderBinding
import com.example.pagandroid.helpers.Datetime

class ListReminderAdapter(
    listReminder: List<GetAllReminderQuery.GetAllNotificationLog>,
    viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> ItemReminderBinding
): RecycleViewAdapter<
        GetAllReminderQuery.GetAllNotificationLog,
        ItemReminderBinding
        >(listReminder, viewBindingFactory) {
    override fun bind(
        binding: ItemReminderBinding,
        item: GetAllReminderQuery.GetAllNotificationLog
    ) {
        val content = HtmlCompat.fromHtml(item.content, HtmlCompat.FROM_HTML_MODE_COMPACT)
        binding.tvUsername.text = item.toName
        binding.tvDatetime.text = Datetime.shard.formatDateTime(item.createdAt.toString())
        binding.tvSubject.text = item.subject
        binding.tvContent.text = content.toString()
        binding.tvReadmore.setOnClickListener {
            binding.tvSubject.setTextColor(Color.parseColor("#888888"))
            binding.tvContent.isSingleLine = false
            binding.tvContent.text = content
            binding.tvContent.setTextColor(Color.parseColor("#888888"))
            binding.tvBtnClose.visibility = View.VISIBLE
            binding.tvReadmore.visibility = View.GONE
            binding.tvBtnClose.setOnClickListener {
                binding.tvContent.isSingleLine = true
                binding.tvContent.text = content.toString()
                binding.tvSubject.setTextColor(Color.BLACK)
                binding.tvContent.setTextColor(Color.parseColor("#676767"))
                binding.tvBtnClose.visibility = View.GONE
                binding.tvReadmore.visibility = View.VISIBLE
                binding.tvBtnClose.setOnClickListener(null)
            }
        }
    }
}