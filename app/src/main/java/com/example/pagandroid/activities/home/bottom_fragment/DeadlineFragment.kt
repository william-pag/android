package com.example.pagandroid.activities.home.bottom_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pagandroid.R
import com.example.pagandroid.databinding.FragmentDeadlineBinding

/**
 * A simple [Fragment] subclass.
 * Use the [DeadlineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeadlineFragment : Fragment() {
    private var _deadlineBinding: FragmentDeadlineBinding? = null
    private val deadlineBinding get() = _deadlineBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _deadlineBinding = FragmentDeadlineBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return this.deadlineBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _deadlineBinding = null
    }
}
