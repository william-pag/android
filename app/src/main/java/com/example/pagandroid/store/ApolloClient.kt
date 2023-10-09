package com.example.pagandroid.store

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import okhttp3.OkHttpClient

class MyApolloClient {
    var client = ApolloClient.Builder()
        .serverUrl("http://103.81.85.228:5000/graphql")
        .build()
    var token: String? = null

    fun setClient(token: String) {
        this.token = token
        this.client = ApolloClient.Builder()
            .serverUrl("http://103.81.85.228:5000/graphql")
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor(token))
                    .build()
            )
            .build()
    }
}

fun createClient(): MyApolloClient {
    val client = MyApolloClient()

    return client
}

val myApolloClient = createClient()
