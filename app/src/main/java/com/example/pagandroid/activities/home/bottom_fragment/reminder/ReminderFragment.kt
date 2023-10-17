package com.example.pagandroid.activities.home.bottom_fragment.reminder

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.R
import com.example.pagandroid.activities.adapter.DropdownEvaluationAdapter
import com.example.pagandroid.activities.home.bottom_fragment.dropdown.IGetUser
import com.example.pagandroid.dao.Reminder
import com.example.pagandroid.databinding.FragmentReminderBinding
import com.example.pagandroid.databinding.ItemReminderBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [ReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReminderFragment : Fragment(), IGetUser {
    private var _reminderBinding: FragmentReminderBinding? = null
    private val reminderBinding get() = _reminderBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _reminderBinding = FragmentReminderBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            setSpinnerUser(layoutInflater.context)
        }
        return this.reminderBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _reminderBinding = null
    }

    private suspend fun setSpinnerUser(context: Context) {
        val users = getAllUsers()
        if (users != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val dropdownEvaluationAdapter = DropdownEvaluationAdapter(
                    context,
                    R.layout.item_evaluation_dropdown_selected,
                    users
                )
                reminderBinding.spinnerUser.adapter = dropdownEvaluationAdapter
                reminderBinding.spinnerUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        val userId: Optional<Double?> = if (users[p2].id == 0.0) {
                            Optional.Absent
                        } else {
                            Optional.present(users[p2].id)
                        }
                        CoroutineScope(Dispatchers.IO).launch {
                            createListReminders(userId)
                        }
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        Log.d("EvaluationAdapter", "NothingSelected")
                    }
                }
            }
        }
    }

    private suspend fun createListReminders(userId: Optional<Double?> = Optional.Absent) {
        val listReminders = Reminder.shard.getAllReminders(userId)
        if (listReminders?.getAllNotificationLogs != null) {
            CoroutineScope(Dispatchers.Main).launch {
                val adapter = ListReminderAdapter(listReminders.getAllNotificationLogs) { inflater, viewGroup, attachToRoot ->
                    ItemReminderBinding.inflate(inflater, viewGroup, attachToRoot)
                }
                reminderBinding.rcvListReminder.layoutManager = LinearLayoutManager(context)
                reminderBinding.rcvListReminder.adapter = adapter
            }
        }
    }
}
