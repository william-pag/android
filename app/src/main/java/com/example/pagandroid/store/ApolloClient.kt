package com.example.pagandroid.store

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient

class MyApolloClient {
    private var _client: ApolloClient? = ApolloClient.Builder()
        .serverUrl("http://103.81.85.228:5000/graphql")
        .build()
    val client get() = _client!!

    val nullableClient get() = _client
    fun setClient(token: String) {
        this._client = ApolloClient.Builder()
            .serverUrl("http://103.81.85.228:5000/graphql")
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
