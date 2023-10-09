package com.example.pagandroid.store

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

fun myStoreContext(): Authorization {
    val auth = Authorization()

    return auth
}

val StoreContext = myStoreContext()
