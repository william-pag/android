package com.example.pagandroid.dao.redis

import redis.clients.jedis.JedisPubSub

class RedisPubSub(private val callback: (String) -> Unit) : JedisPubSub() {
    override fun onMessage(channel: String?, message: String?) {
        callback.invoke(message ?: "")
    }
}
