package com.example.pagandroid.room

import android.content.Context

class SharedPreference(private val context: Context) {
    private val build = context.getSharedPreferences(context.packageName.toString(), Context.MODE_PRIVATE)

    fun addToken(value: String) {
        build.edit().putString("token", value).apply()
    }

    fun getToken(): String? {
        return build.getString("token", null)
    }
}
