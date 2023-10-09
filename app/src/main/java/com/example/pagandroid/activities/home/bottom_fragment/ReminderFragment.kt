package com.example.pagandroid.activities.home.bottom_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pagandroid.R
import com.example.pagandroid.databinding.FragmentReminderBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ReminderFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReminderFragment : Fragment() {
    private var _reminderBinding: FragmentReminderBinding? = null
    private val reminderBinding get() = _reminderBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _reminderBinding = FragmentReminderBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return this.reminderBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _reminderBinding = null
    }
}
