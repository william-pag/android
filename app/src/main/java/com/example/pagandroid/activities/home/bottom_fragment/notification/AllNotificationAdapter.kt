package com.example.pagandroid.activities.home.bottom_fragment.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.pagandroid.GetAllNotificationShortsQuery
import com.example.pagandroid.activities.adapter.RecycleViewAdapter
import com.example.pagandroid.databinding.ItemNotificationBinding
import com.example.pagandroid.helpers.Datetime

class AllNotificationAdapter(
    listNotification: List<GetAllNotificationShortsQuery.GetAllNotificationShort>,
    viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> ItemNotificationBinding
): RecycleViewAdapter<
        GetAllNotificationShortsQuery.GetAllNotificationShort,
        ItemNotificationBinding
        >(listNotification, viewBindingFactory) {
    override fun bind(
        binding: ItemNotificationBinding,
        item: GetAllNotificationShortsQuery.GetAllNotificationShort
    ) {
        binding.tvNotiSubject.text = item.subject
        binding.tvNotiDatetime.text = Datetime.shard.formatDateTime(item.updatedAt.toString())
    }
}