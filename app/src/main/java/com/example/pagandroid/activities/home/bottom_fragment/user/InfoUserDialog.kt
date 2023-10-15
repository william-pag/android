package com.example.pagandroid.activities.home.bottom_fragment.user

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import com.example.pagandroid.GetOneUserQuery
import com.example.pagandroid.activities.home.bottom_fragment.dropdown.IGetUser
import com.example.pagandroid.dao.User
import com.example.pagandroid.databinding.InfoUserDialogBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InfoUserDialog(context: Context, private val userId: Double): Dialog(context), IGetUser {
    private lateinit var infoBinding: InfoUserDialogBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        infoBinding = InfoUserDialogBinding.inflate(layoutInflater)
        setContentView(infoBinding.root)
        createDialogUser(userId = userId)
    }

    private fun createDialogUser(userId: Double) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = getDetailUser(userId) ?: return@launch
            CoroutineScope(Dispatchers.Main).launch {
                Glide.with(context).load(user.image).into(infoBinding.imgAvatar)
                infoBinding.tvUsername.text = user.name
                infoBinding.tvTitle.text = user.title?.name
                infoBinding.tvEmail.text = user.email
                infoBinding.tvLastLogin.text = user.lastLogin.toString()
                infoBinding.tvLocation.text = user.location?.name
                infoBinding.tvDepartment.text = user.department?.name
                infoBinding.tvEvaluator.text = user.evaluator?.name
                infoBinding.tvForm.text = user.evaluationType?.name
            }
        }
    }
}
