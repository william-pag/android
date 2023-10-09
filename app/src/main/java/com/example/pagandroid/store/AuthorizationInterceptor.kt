package com.example.pagandroid.store

import okhttp3.Interceptor
import okhttp3.Response

class AuthorizationInterceptor(private val token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .apply {
                val token = "Bearer $token"
                addHeader("Authorization", token)
            }
            .build()
        return chain.proceed(request)
    }
}
