package com.example.pagandroid.dao.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class RedisClient {
    companion object {
        val shared = RedisClient()
    }
    private val mapListener = mutableMapOf<String, JedisPubSub>()
    private val jedis = Jedis("103.81.85.228", 6379)

    fun sub(channel: String, listener: JedisPubSub) {
        mapListener.set(channel, listener)
        jedis.subscribe(listener, channel)
    }

    fun unsub(channel: String) {
        mapListener[channel]?.unsubscribe(channel)
    }

    fun disconn() {
        jedis.disconnect()
    }
}
