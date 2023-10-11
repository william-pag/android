package com.example.pagandroid.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pagandroid.activities.home.BottomNavigatorActivity
import com.example.pagandroid.controllers.login.GlobalAction
import com.example.pagandroid.dao.User
import com.example.pagandroid.databinding.ActivitySplashScreenBinding
import com.example.pagandroid.room.RoomController
import com.example.pagandroid.room.SharedPreference
import com.example.pagandroid.room.entities.UserLogin
import com.example.pagandroid.store.StoreContext
import com.example.pagandroid.store.myApolloClient
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    private val TAG = "plashScreen"
    private lateinit var splashBinding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.splashBinding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(this.splashBinding.root)
        StoreContext.setContext(this)
        runBlocking(Dispatchers.IO) {
            checkLogin()
        }
    }


    private suspend fun checkLogin() {
        val token = SharedPreference(this@SplashScreenActivity).getToken()
        if (token != null) {
            myApolloClient.setClient(token)
            val data = User.shard.getMe()
            if (data != null) {
                CoroutineScope(Dispatchers.IO).launch {
                    val userLogin = UserLogin(0, data.me.name, data.me.email,  data.me.cycleId, data.me.image)
                    val controller = RoomController(this@SplashScreenActivity)
                    controller.deleteUser(userLogin)
                    controller.addUserLogin(userLogin)
                }
                CoroutineScope(Dispatchers.Main).launch {
                    val intent = Intent(this@SplashScreenActivity, BottomNavigatorActivity::class.java)
                    this@SplashScreenActivity.startActivity(intent)
                }
            } else {
                redirectLogin()
            }
        } else {
            redirectLogin()
        }
    }

    private fun redirectLogin() {
        CoroutineScope(Dispatchers.Main).launch {
            GlobalAction.shared.redirectLogin()
        }
    }

    override fun onDestroy() {
        myApolloClient.removeClient()
        StoreContext.removeContext()
        super.onDestroy()
    }
}
