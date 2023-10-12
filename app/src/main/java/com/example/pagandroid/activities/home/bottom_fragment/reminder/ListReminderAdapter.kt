package com.example.pagandroid.activities.home.bottom_fragment.reminder

import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pagandroid.GetAllReminderQuery
import com.example.pagandroid.databinding.ItemReminderBinding
import org.jsoup.Jsoup
import org.jsoup.safety.Safelist
import java.util.Date

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

    override fun onBindViewHolder(holder: ItemReminder, position: Int) {
        with(holder) {
            with(listReminder[position]) {
                itemBinding.tvUsername.text = this.toName
                itemBinding.tvDatetime.text = this.createdAt.toString()
                itemBinding.tvSubject.text = this.subject
                itemBinding.tvContent.text = Jsoup.parse(this.content).text()
                itemBinding.tvReadmore.setOnClickListener {
                    itemBinding.tvSubject.setTextColor(Color.parseColor("#888888"))
                    itemBinding.tvContent.isSingleLine = false
                    itemBinding.tvContent.text = Html.fromHtml(this.content)
                    itemBinding.tvContent.setTextColor(Color.parseColor("#888888"))
                    itemBinding.tvBtnClose.visibility = View.VISIBLE

                    itemBinding.tvBtnClose.setOnClickListener {
                        itemBinding.tvContent.isSingleLine = true
                        itemBinding.tvContent.text = Jsoup.parse(this.content).text()
                        itemBinding.tvSubject.setTextColor(Color.parseColor("#000"))
                        itemBinding.tvContent.setTextColor(Color.parseColor("#676767"))
                    }
                }
            }
        }
    }
}
