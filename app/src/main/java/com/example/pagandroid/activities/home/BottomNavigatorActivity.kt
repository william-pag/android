package com.example.pagandroid.activities.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pagandroid.R
import com.example.pagandroid.activities.home.bottom_fragment.deadline.DeadlineFragment
import com.example.pagandroid.activities.home.bottom_fragment.home.HomeFragment
import com.example.pagandroid.activities.home.bottom_fragment.reminder.ReminderFragment
import com.example.pagandroid.activities.home.bottom_fragment.user.InfoUserDialog
import com.example.pagandroid.activities.home.bottom_fragment.user.UserFragment
import com.example.pagandroid.activities.home.evaluation_fragment.user_action.UserActionFragment
import com.example.pagandroid.databinding.ActivityBottomNavigatorBinding
import com.example.pagandroid.room.RoomController
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BottomNavigatorActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var bottomNavigatorBinding: ActivityBottomNavigatorBinding
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.bottomNavigatorBinding = ActivityBottomNavigatorBinding.inflate(layoutInflater)
        setContentView(this.bottomNavigatorBinding.root)
        val drawer = this.bottomNavigatorBinding.drawerLayout
        val toggle = ActionBarDrawerToggle(this, drawer, null, R.string.open_drawer, R.string.close_drawer)
        drawer.addDrawerListener(toggle)
        val drawerLayout = bottomNavigatorBinding.frameDrawer.drawerLayout
        toggle.syncState()
        bottomNavigatorBinding.bottomNavigation.setOnItemSelectedListener { item ->
            onNavigationItemSelected(item)
        }
        // Navigate to the Home fragment by default
        supportFragmentManager.beginTransaction()
            .replace(R.id.content_frame, UserActionFragment())
            .commit()
        bottomNavigatorBinding.imgLogo.setOnClickListener {
            drawer.open()
        }
        CoroutineScope(Dispatchers.IO).launch {
            val user = RoomController(this@BottomNavigatorActivity).getMe()
            CoroutineScope(Dispatchers.Main).launch {
                bottomNavigatorBinding.userName.text = user.name.replace(" ", "\n")
                Glide.with(this@BottomNavigatorActivity).load(user.image).into(drawerLayout.drawerAvatar)
                Glide.with(this@BottomNavigatorActivity).load(user.image).into(bottomNavigatorBinding.userAvatar)
                drawerLayout.tvUsername.text = user.name
                drawerLayout.tvEmail.text = user.email
                bottomNavigatorBinding.userAvatar.setOnClickListener {
                    InfoUserDialog(bottomNavigatorBinding.root.context, user.userId).show()
                }
            }
        }

        setBadgeReminder(5)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, HomeFragment())
                    .commit()
                return true
            }
            R.id.nav_profile -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.content_frame, UserFragment())
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

    @SuppressLint("ResourceType")
    private fun setBadgeReminder(num: Int, resourceId: Int = R.id.nav_reminder, isShow: Boolean = false) {
        val badgeView = this.bottomNavigatorBinding.bottomNavigation.getOrCreateBadge(resourceId)
        badgeView.number = num
        badgeView.isVisible = isShow
    }
}
