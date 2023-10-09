package com.example.pagandroid.activities.home.bottom_fragment.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import com.example.pagandroid.GetAllStrategiesQuery
import com.example.pagandroid.R
import com.example.pagandroid.controllers.login.GlobalAction
import com.example.pagandroid.dao.Overview
import com.example.pagandroid.databinding.FragmentHomeBinding
import kotlinx.coroutines.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    private val TAG = "FragmentHomeBinding"
    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.Main).launch {
            setSpinnerStrategy(layoutInflater.context)
        }
        return this.homeBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _homeBinding = null
    }

    private fun setSpinnerStrategy(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val allStrategies = getAllStrategies()
            if (allStrategies != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val dropdownEvaluationAdapter = DropdownEvaluationAdapter(context, R.layout.item_evaluation_dropdown_selected,
                        arrayOf(allStrategies)
                    )

                    homeBinding.spinnerStrategy.adapter = dropdownEvaluationAdapter

                    homeBinding.spinnerStrategy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            Toast.makeText(context, dropdownEvaluationAdapter.getItem(p2)?.getAllStrategies?.get(p2)?.name
                                ?: "null", Toast.LENGTH_LONG).show()
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
                }

            }
        }
    }

    private suspend fun getAllStrategies(): GetAllStrategiesQuery.Data? {
        return Overview.shard.getAllStrategies()
    }
}
