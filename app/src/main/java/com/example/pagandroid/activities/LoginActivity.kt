package com.example.pagandroid.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.pagandroid.dao.User
import com.example.pagandroid.databinding.ActivityLoginBinding
import com.example.pagandroid.room.SharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private var canPress = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(this.loginBinding.root)
        this.loginBinding.btnLogin.setOnClickListener {
            if (this.canPress) {
                this.canPress = false
                this.loginBinding.btnLogin.alpha = 0.5f
                this.loginBinding.loadingLogin.visibility = View.VISIBLE
                login()
            }
        }
    }

    private fun login() {
        CoroutineScope(Dispatchers.IO).launch {
            val email = loginBinding.loginEmail.text.trim().toString()
            val password = loginBinding.loginPassword.text.toString()
            val token = User.shard.login(email, password)
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            withContext(Dispatchers.IO) {
                if (token != null) {
                    SharedPreference(this@LoginActivity).addToken(token)
                }
            }
            CoroutineScope(Dispatchers.Main).launch {
                canPress = true
                loginBinding.btnLogin.alpha = 1f
                loginBinding.loadingLogin.visibility = View.GONE
                if (token != null) {
                    val intent = Intent(this@LoginActivity, SplashScreenActivity::class.java)
                    this@LoginActivity.startActivity(intent)
                } else {
                    loginBinding.loginPassword.text.clear()
                }
            }
        }
    }
}
