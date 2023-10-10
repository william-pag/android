package com.example.pagandroid.activities.home.bottom_fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListPopupWindow
import android.widget.Spinner
import com.example.pagandroid.GetAllDepartmentsQuery
import com.example.pagandroid.GetAllStrategiesQuery
import com.example.pagandroid.R
import com.example.pagandroid.dao.Overview
import com.example.pagandroid.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import com.apollographql.apollo3.api.Optional
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import okhttp3.internal.notifyAll


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
            setSpinnerDepartment(layoutInflater.context)
            createChartOverview(Optional.Absent, Optional.Absent)
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
                    val dropdownEvaluationAdapter = DropdownEvaluationAdapter(
                        context,
                        R.layout.item_evaluation_dropdown_selected,
                        allStrategies
                    )
                    homeBinding.spinnerStrategy.adapter = dropdownEvaluationAdapter
                    homeBinding.spinnerStrategy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            val strategyId: Optional<Double?> = if (allStrategies[p2].id == 0.0) {
                                Optional.Absent
                            } else {
                                Optional.present(allStrategies[p2].id)
                            }

                            if (p2 != 0) {
                                setSpinnerDepartment(context, strategyId)
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            Log.d("EvaluationAdapter", "NothingSelected")
                        }
                    }
                }

            }
        }
    }

    private fun setSpinnerDepartment(context: Context, strategyId: Optional<Double?> = Optional.Absent) {
        CoroutineScope(Dispatchers.IO).launch {
            val allDepartments = getAllDepartments(strategyId = strategyId)
            if (allDepartments != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val dropdownEvaluationAdapter = DropdownEvaluationAdapter(
                        context,
                        R.layout.item_evaluation_dropdown_selected,
                        allDepartments
                    )
                    dropdownEvaluationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    homeBinding.spinnerDepartment.adapter = dropdownEvaluationAdapter
                    homeBinding.spinnerDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            if (p2 != 0) {
                                val departmentIds = Optional.present(listOf(allDepartments[p2].id))
                                createChartOverview(strategyId, departmentIds)
                            }
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            Log.d("EvaluationAdapter", "NothingSelected")
                        }

                    }
                }

            }
        }
    }

    private suspend fun getAllStrategies(): MutableList<GetAllStrategiesQuery.GetAllStrategy>? {
        val strategies =  Overview.shard.getAllStrategies()
        val arrStrategy = strategies?.getAllStrategies?.toMutableList()
        arrStrategy?.add(0, GetAllStrategiesQuery.GetAllStrategy(0.0, "Select Strategy"))
        return arrStrategy
    }
    private suspend fun getAllDepartments(strategyId: Optional<Double?> = Optional.Absent): MutableList<GetAllDepartmentsQuery.GetAllDepartment>? {
        Log.d("strategyId", "$strategyId")
        val setDepartmentId = mutableSetOf<Double>()
        val departments =  Overview.shard.getAllDepartments(strategyId = strategyId)
        var arrDepartments = departments?.getAllDepartments?.toMutableList()
        if (strategyId == Optional.Absent) {
            val mapDepartment = mutableMapOf<String, GetAllDepartmentsQuery.GetAllDepartment>()
            arrDepartments?.forEach { getAllDepartment ->
                mapDepartment[getAllDepartment.name] = getAllDepartment
            }
            arrDepartments = mapDepartment.values.toMutableList()
            mapDepartment.clear()
        } else {
            arrDepartments?.forEach { getAllDepartment ->
                setDepartmentId.plus(getAllDepartment.id)
            }
        }
        val departmentIds = if (strategyId == Optional.Absent) {
            Optional.Absent
        } else {
            Optional.present(setDepartmentId.toList())
        }
        createChartOverview(strategyId = strategyId, departmentIds = departmentIds)
        arrDepartments?.add(0, GetAllDepartmentsQuery.GetAllDepartment(0.0, "Select Department", null))
        setDepartmentId.clear()
        return arrDepartments
    }

    private fun createChartOverview(strategyId: Optional<Double?> = Optional.Absent, departmentIds: Optional<List<Double>?> = Optional.Absent)  {
        CoroutineScope(Dispatchers.IO).launch {
            val overallProgress = Overview.shard.getOverallProgress(departmentIds, strategyId)
            Log.d(TAG, "${overallProgress?.overallProgress}")
            CoroutineScope(Dispatchers.Main).launch {
                val entries = arrayListOf(
                    PieEntry(10f, "1"),
                    PieEntry(10f, "2"),
                    PieEntry(10f, "3")
                )
                val pieDataSet = PieDataSet(entries, "Overall")
                homeBinding.chartOverallProgress.data = PieData(pieDataSet)
                homeBinding.chartOverallProgress.notifyDataSetChanged()
            }
        }
    }
}
