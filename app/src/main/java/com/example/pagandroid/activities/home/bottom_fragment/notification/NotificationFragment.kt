package com.example.pagandroid.activities.home.bottom_fragment.notification

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagandroid.R
import com.example.pagandroid.controllers.login.GlobalAction
import com.example.pagandroid.dao.Notification
import com.example.pagandroid.databinding.FragmentNotificationBinding
import com.example.pagandroid.databinding.ItemNotificationBinding
import com.example.pagandroid.databinding.ItemReminderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [NotificationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NotificationFragment : Fragment() {
    private lateinit var context: Context
    private var _notificationBinding: FragmentNotificationBinding? = null
    private val binding get() = _notificationBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        context = inflater.context
        _notificationBinding = FragmentNotificationBinding.inflate(inflater, container, false)
        createListNotifications()
        binding.btnClearAll.setOnClickListener {
            binding.progressLoading.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val isSuccess = Notification.shard.clearAllNotifications()
                if (isSuccess) {
                    createListNotifications()
                    GlobalAction
                        .shared
                        .makeToast("Clear all notifications successfully")
                } else {
                    GlobalAction
                        .shared
                        .makeToast("Can't not clear all notifications")
                }
                CoroutineScope(Dispatchers.Main).launch {
                    binding.progressLoading.visibility = View.GONE
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _notificationBinding = null
    }

    private fun createListNotifications() {
        CoroutineScope(Dispatchers.IO).launch {
            val list = Notification.shard.getAllNotifications()?.getAllNotificationShorts ?: return@launch
            CoroutineScope(Dispatchers.Main).launch {
                binding.rcvAllNotifications.layoutManager = LinearLayoutManager(context)
                val adapter = AllNotificationAdapter(list) { inflater, viewGroup, attachToRoot ->
                    ItemNotificationBinding.inflate(inflater, viewGroup, attachToRoot)
                }
                binding.rcvAllNotifications.adapter = adapter
            }
        }
    }
}
