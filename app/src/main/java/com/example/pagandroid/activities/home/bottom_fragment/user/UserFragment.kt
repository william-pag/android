package com.example.pagandroid.activities.home.bottom_fragment.user

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagandroid.GetAllUserNameQuery
import com.example.pagandroid.activities.home.bottom_fragment.dropdown.IGetUser
import com.example.pagandroid.databinding.FragmentUserBinding
import com.example.pagandroid.databinding.ItemDeadlineBinding
import com.example.pagandroid.databinding.ItemUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [UserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UserFragment : Fragment(), IGetUser {
    private var _userBinding: FragmentUserBinding? = null
    private val userBinding get() = _userBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _userBinding = FragmentUserBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            createListUser(inflater.context)
        }
        return this.userBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _userBinding = null
    }

    private suspend fun createListUser(context: Context) {
        val listUser = getAllUsers() ?: return
        listUser.removeAt(0)
        CoroutineScope(Dispatchers.Main).launch {
            val adapter = ListUserAdapter(listUser) { inflater, viewGroup, attachToRoot ->
                ItemUserBinding.inflate(inflater, viewGroup, attachToRoot)
            }
            userBinding.rcvAllUser.adapter = adapter
            userBinding.rcvAllUser.layoutManager = LinearLayoutManager(context)
        }
    }
}
