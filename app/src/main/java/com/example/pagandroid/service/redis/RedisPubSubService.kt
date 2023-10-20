package com.example.pagandroid.service.redis

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.pagandroid.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class RedisPubSubService : Service() {
    private lateinit var jedis: Jedis
    private lateinit var jedisPubSub: JedisPubSub
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "CHANNEL_ID",
                "Your Channel Name",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        CoroutineScope(Dispatchers.IO).launch {
            jedisPubSub = object : JedisPubSub() {
                override fun onMessage(channel: String?, message: String?) {
                    super.onMessage(channel, message)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showNotification()
                    }
                }
            }
            jedis = Jedis("103.81.85.228", 6379, false)

            jedis.subscribe(jedisPubSub, "FIRST-CHANNEL")
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Thread(Runnable {
            jedisPubSub = object : JedisPubSub() {
                override fun onMessage(channel: String?, message: String?) {
                    super.onMessage(channel, message)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showNotification()
                    }
                }
            }
            jedis = Jedis("103.81.85.228", 6379, false)

            jedis.subscribe(jedisPubSub, "FIRST-CHANNEL")
        }).start()
        return START_STICKY
    }

    override fun onDestroy() {
        jedisPubSub.unsubscribe()
        jedis.close()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun  showNotification() {
        val notificationBuilder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_reminder)
            .setContentTitle("Notification Title")
            .setContentText("Notification Content")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(1, notificationBuilder.build())
    }
}