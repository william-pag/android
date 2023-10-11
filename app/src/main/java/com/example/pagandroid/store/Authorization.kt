package com.example.pagandroid.store

import android.annotation.SuppressLint
import android.content.Context

class Authorization {
    private var _context: Context? = null

    fun setContext(viewContext: Context) {
        this._context = viewContext
    }

    val context get() = _context!!

    fun removeContext() {
        this._context = null
    }
}


@SuppressLint("StaticFieldLeak")
val StoreContext = Authorization()
