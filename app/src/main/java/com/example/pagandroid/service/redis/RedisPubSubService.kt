package com.example.pagandroid.service.redis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.pagandroid.R
import com.example.pagandroid.dao.redis.RedisClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class RedisPubSubService : JobService() {
    private val TAG = "FIRST-CHANNEL-FIRST"
    private lateinit var notificationManager: NotificationManager
    private var isJobCanceled = false
    private val channel = "FIRST-CHANNEL"
    override fun onStartJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Thread {
                startListener()
            }.start()
        }
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob")
        RedisClient.shared.unsub("FIRST-CHANNEL")
        RedisClient.shared.disconn()
        isJobCanceled = true
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun createNotification() {
        Log.d(TAG, "onCreate")
        notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "Your Channel Name",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun startListener() {
        if (isJobCanceled) {
            return
        }
        createNotification()
        val jedisPubSub = object : JedisPubSub() {
            override fun onMessage(channel: String?, message: String?) {
                super.onMessage(channel, message)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Log.d(TAG, "$channel -- $message")
                    showNotification(message)
                }
            }
        }
        RedisClient.shared.sub(channel, jedisPubSub)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun  showNotification(message: String?) {
        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_reminder)
            .setContentTitle("Notification")
            .setContentText(message ?: "Notification")
            .setPriority(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setAutoCancel(true)
        notificationManager.notify(1, notificationBuilder.build())
    }
}
