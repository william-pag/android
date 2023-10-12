package com.example.pagandroid.activities.home

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.example.pagandroid.R
import com.example.pagandroid.activities.home.bottom_fragment.DeadlineFragment
import com.example.pagandroid.activities.home.bottom_fragment.home.HomeFragment
import com.example.pagandroid.activities.home.bottom_fragment.ProfileFragment
import com.example.pagandroid.activities.home.bottom_fragment.reminder.ReminderFragment
import com.example.pagandroid.databinding.ActivityBottomNavigatorBinding
import com.example.pagandroid.room.RoomController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomNavigatorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var bottomNavigatorBinding: ActivityBottomNavigatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.bottomNavigatorBinding = ActivityBottomNavigatorBinding.inflate(layoutInflater)
        setContentView(this.bottomNavigatorBinding.root)
        bottomNavigatorBinding.bottomNavigation.setOnItemSelectedListener { item ->
            onNavigationItemSelected(item)
        }
        // Navigate to the Home fragment by default
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, ReminderFragment())
            .commit()
        CoroutineScope(Dispatchers.IO).launch {
            setMainUser()
        }

        setBadgeReminder(5)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                item.icon?.setLevel(5)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, HomeFragment())
                    .commit()
                return true
            }
            R.id.nav_profile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, ProfileFragment())
                    .commit()
                return true
            }
            R.id.nav_deadline -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, DeadlineFragment())
                    .commit()
                return true
            }
            R.id.nav_reminder -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, ReminderFragment())
                    .commit()
                return true
            }
            else -> {
                return false
            }
        }
    }

    fun setMainUser() {
        val user = RoomController(this).getMe()
        CoroutineScope(Dispatchers.Main).launch {
            bottomNavigatorBinding.userName.text = user.name.replace(" ", "\n")
            Glide.with(this@BottomNavigatorActivity).load(user.image).into(bottomNavigatorBinding.userAvatar)
        }
    }

    @SuppressLint("ResourceType")
    private fun setBadgeReminder(num: Int, resourceId: Int = R.id.nav_reminder, isShow: Boolean = false) {
        val badgeView = this.bottomNavigatorBinding.bottomNavigation.getOrCreateBadge(resourceId)
        badgeView.number = num
        badgeView.isVisible = isShow
    }
}
