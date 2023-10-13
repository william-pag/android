package com.example.pagandroid.activities.home.bottom_fragment.reminder

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pagandroid.GetAllReminderQuery
import com.example.pagandroid.databinding.ItemReminderBinding
import com.example.pagandroid.helpers.Datetime

class ListReminderAdapter(private val listReminder: List<GetAllReminderQuery.GetAllNotificationLog>):
    RecyclerView.Adapter<ListReminderAdapter.ItemReminder>() {
    inner class ItemReminder(val itemBinding: ItemReminderBinding): RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemReminder {
        return ItemReminder(
            ItemReminderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return this.listReminder.size
    }

    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: ItemReminder, position: Int) {
        with(holder) {
            with(listReminder[position]) {
                val content = HtmlCompat.fromHtml(this.content, HtmlCompat.FROM_HTML_MODE_COMPACT)
                itemBinding.tvUsername.text = this.toName
                itemBinding.tvDatetime.text = Datetime.shard.formatDateTime(this.createdAt.toString())
                itemBinding.tvSubject.text = this.subject
                itemBinding.tvContent.text = content.toString()
                itemBinding.tvReadmore.setOnClickListener {
                    itemBinding.tvSubject.setTextColor(Color.parseColor("#888888"))
                    itemBinding.tvContent.isSingleLine = false
                    itemBinding.tvContent.text = content
                    itemBinding.tvContent.setTextColor(Color.parseColor("#888888"))
                    itemBinding.tvBtnClose.visibility = View.VISIBLE
                    itemBinding.tvReadmore.visibility = View.GONE
                    itemBinding.tvBtnClose.setOnClickListener {
                        itemBinding.tvContent.isSingleLine = true
                        itemBinding.tvContent.text = content.toString()
                        itemBinding.tvSubject.setTextColor(Color.BLACK)
                        itemBinding.tvContent.setTextColor(Color.parseColor("#676767"))
                        itemBinding.tvBtnClose.visibility = View.GONE
                        itemBinding.tvReadmore.visibility = View.VISIBLE
                        itemBinding.tvBtnClose.setOnClickListener(null)
                    }
                }
            }
        }
    }
}
