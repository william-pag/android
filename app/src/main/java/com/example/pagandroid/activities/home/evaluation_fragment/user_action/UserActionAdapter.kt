package com.example.pagandroid.activities.home.evaluation_fragment.user_action

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.pagandroid.GetAllUserNameQuery
import com.example.pagandroid.activities.adapter.RecycleViewAdapter
import com.example.pagandroid.databinding.ItemUserBinding

class UserActionAdapter(
    listUser: List<GetAllUserNameQuery.GetAllUser>,
    viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> ItemUserBinding
): RecycleViewAdapter<
        GetAllUserNameQuery.GetAllUser,
        ItemUserBinding
        >(listUser, viewBindingFactory) {
    override fun bind(binding: ItemUserBinding, item: GetAllUserNameQuery.GetAllUser) {
        binding.tvUsername.text = item.name
        binding.tvTitle.text = item.title?.name ?: "Title"
        Glide.with(binding.root.context).load(item.image).into(binding.imgAvatar)
        binding.layoutItemUser.setOnClickListener {
            UserActionDialog(binding.root.context, item.name).show()
        }
    }
}