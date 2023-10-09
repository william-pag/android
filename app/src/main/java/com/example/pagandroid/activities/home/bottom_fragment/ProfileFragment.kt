package com.example.pagandroid.activities.home.bottom_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pagandroid.R
import com.example.pagandroid.databinding.FragmentProfileBinding

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var _profileBinding: FragmentProfileBinding? = null
    private val profileBinding get() = _profileBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _profileBinding = FragmentProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return this.profileBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _profileBinding = null
    }
}
