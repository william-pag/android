package com.example.pagandroid.controllers.login

import android.content.Context
import android.content.Intent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.pagandroid.activities.LoginActivity
import com.example.pagandroid.store.StoreContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlobalAction() {
    private val storeContext = StoreContext

    companion object {
        val shared = GlobalAction()
    }
    fun redirectLogin() {
        val intent = Intent(this.storeContext.context, LoginActivity::class.java)
        this.storeContext.context.startActivity(intent)
    }
    fun makeToast(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(storeContext.context, message, Toast.LENGTH_LONG).show()
        }
    }
}
