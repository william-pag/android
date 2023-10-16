package com.example.pagandroid.activities.home.evaluation_fragment.adapter

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagandroid.databinding.FirstContentContributorBinding
import com.example.pagandroid.databinding.ItemContributorStatusBinding
import com.example.pagandroid.databinding.ItemHeaderContributorBinding
import com.example.pagandroid.databinding.RcvListStatusContributorBinding
import com.example.pagandroid.model.LOCsWaitApproval.Header
import com.example.pagandroid.model.LOCsWaitApproval.ILOCsWaitApproval
import com.example.pagandroid.model.LOCsWaitApproval.Percentage
import com.example.pagandroid.model.LOCsWaitApproval.UserWaitApproval

class StickyHeaderAdapter(private val list: List<ILOCsWaitApproval>) :
    RecyclerView.Adapter<StickyHeaderAdapter.Companion.BaseViewHolder<*>>() {

    inner class HeaderViewHolder(val binding: ItemHeaderContributorBinding) :
        BaseViewHolder<ItemHeaderContributorBinding>(binding.root)

    inner class FirstViewHolder(val binding: FirstContentContributorBinding) :
        BaseViewHolder<FirstContentContributorBinding>(binding.root)

    inner class SecondViewHolder(val binding: RcvListStatusContributorBinding) :
        BaseViewHolder<RcvListStatusContributorBinding>(binding.root)

    companion object {
        abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view)
    }

    enum class ViewType {
        Header,
        FirstContent,
        SecondContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            ViewType.Header.ordinal -> {
                val layout = ItemHeaderContributorBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HeaderViewHolder(layout)
            }

            ViewType.FirstContent.ordinal -> {
                val layout = FirstContentContributorBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                FirstViewHolder(layout)
            }

            ViewType.SecondContent.ordinal -> {
                val layout = RcvListStatusContributorBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SecondViewHolder(layout)
            }

            else -> throw IllegalArgumentException("Not a layout")
        }
    }

    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.binding.tvHeaderContributor.text = (list[position] as Header).title
            }
            is FirstViewHolder -> {
                holder.binding.tvPercentStatus.text = "Status ${(list[position] as Percentage).percent}% Completed"
            }
            is SecondViewHolder -> {
                val adapter = UserStatusAdapter((list[position] as UserWaitApproval).users) { inflater, viewGroup, attachToRoot ->
                    ItemContributorStatusBinding.inflate(inflater, viewGroup, attachToRoot)
                }
                holder.binding.rcvListContributorStatus.layoutManager = LinearLayoutManager(holder.binding.root.context)
                holder.binding.rcvListContributorStatus.adapter = adapter
                holder.binding.rcvListContributorStatus.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        var initialTouchY = 0F;
                        val action = e.action
                        val y = e.y
                        when (action) {
                            MotionEvent.ACTION_DOWN -> {
                                // Save the initial touch position
                                initialTouchY = y
                                rv.parent.requestDisallowInterceptTouchEvent(true)
                            }
                            MotionEvent.ACTION_MOVE -> {
                                val dy = y - initialTouchY
                                if (rv.canScrollVertically(-1) && dy > 0 || rv.canScrollVertically(1) && dy < 0) {
                                    // InnerRecyclerView is being scrolled vertically, allow the outerRecyclerView to handle the touch event
                                    rv.parent.requestDisallowInterceptTouchEvent(false)
                                }
                            }
                        }
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }
                })
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position]) {
            is Header -> ViewType.Header.ordinal
            is Percentage -> ViewType.FirstContent.ordinal
            is UserWaitApproval -> ViewType.SecondContent.ordinal
            else -> -1
        }
    }

}