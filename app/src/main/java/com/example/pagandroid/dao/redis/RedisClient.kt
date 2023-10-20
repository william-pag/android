package com.example.pagandroid.dao.redis

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.JedisPubSub

class RedisClient {
    private val jedis = Jedis("103.81.85.228", 6379, false)
    private fun redisConfig(): JedisPoolConfig {
        val redisCon = JedisPoolConfig()
        redisCon.maxTotal = 100
        redisCon.maxIdle = 100
        redisCon.minIdle = 10
        redisCon.blockWhenExhausted = true
        return redisCon
    }
    companion object {
        val shared = RedisClient()
    }

    fun publish(channel: String, message: String) {
        jedis.publish(channel, message)
    }

    fun subscribe(channel: String, listener: JedisPubSub) {
        jedis.subscribe(listener, channel)
    }

    fun closePool() {
        jedis.close()
    }
}
