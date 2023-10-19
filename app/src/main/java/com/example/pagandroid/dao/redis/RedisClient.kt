package com.example.pagandroid.dao.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPubSub

class RedisClient {
    private val jedis = Jedis("103.81.85.228", 6379)
    companion object {
        val shared = RedisClient()
    }

    fun publish(channel: String, message: String) {
        jedis.publish(channel, message)
    }

    fun subscribe(channel: String, listener: RedisPubSub) {
        jedis.subscribe(listener, channel)
    }
}
