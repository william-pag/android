package com.example.pagandroid.activities.home.evaluation_fragment.user_action

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagandroid.activities.home.bottom_fragment.dropdown.IGetUser
import com.example.pagandroid.databinding.FragmentUserActionBinding
import com.example.pagandroid.databinding.ItemUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [UserActionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserActionFragment : Fragment(), IGetUser {
    private var _userActionBinding: FragmentUserActionBinding? = null
    private val binding get() = _userActionBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _userActionBinding = FragmentUserActionBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            createListUser(inflater.context)
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _userActionBinding = null
    }

    private suspend fun createListUser(context: Context) {
        val listUser = getAllUsers() ?: return
        listUser.removeAt(0)
        CoroutineScope(Dispatchers.Main).launch {
            val adapter = UserActionAdapter(listUser) { inflater, viewGroup, attachToRoot ->
                ItemUserBinding.inflate(inflater, viewGroup, attachToRoot)
            }
            binding.rcvUserAction.adapter = adapter
            binding.rcvUserAction.layoutManager = LinearLayoutManager(context)
        }
    }
}