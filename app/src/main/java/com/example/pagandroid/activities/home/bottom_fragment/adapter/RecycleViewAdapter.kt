package com.example.pagandroid.activities.home.bottom_fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class RecycleViewAdapter<T, VB : ViewBinding>(
    private val data: List<T>,
    private val viewBindingFactory: (LayoutInflater, ViewGroup, Boolean) -> VB
) : RecyclerView.Adapter<RecycleViewAdapter.ItemViewHolder<VB>>() {
    class ItemViewHolder<VB : ViewBinding>(val viewBinding: VB) : RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<VB> {
        val viewBinding = viewBindingFactory(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder<VB>, position: Int) {
        val item = data[position]
        bind(holder.viewBinding, item)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    abstract fun bind(binding: VB, item: T)
}