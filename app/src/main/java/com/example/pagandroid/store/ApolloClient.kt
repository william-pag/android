package com.example.pagandroid.store

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient

class MyApolloClient {
    private val url = "http://192.168.1.196:5000/graphql"
    private var _client: ApolloClient? = ApolloClient.Builder()
        .serverUrl(this.url)
        .build()
    val client get() = _client!!

    val nullableClient get() = _client
    fun setClient(token: String) {
        this._client = ApolloClient.Builder()
            .serverUrl(this.url)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor(token))
                    .build()
            )
            .build()
    }
    fun removeClient() {
        this._client = null
    }
}

val myApolloClient = MyApolloClient()
