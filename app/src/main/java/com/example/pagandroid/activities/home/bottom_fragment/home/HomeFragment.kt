package com.example.pagandroid.activities.home.bottom_fragment.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetAllDepartmentsQuery
import com.example.pagandroid.GetAllStrategiesQuery
import com.example.pagandroid.R
import com.example.pagandroid.dao.Overview
import com.example.pagandroid.databinding.FragmentHomeBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@SuppressLint("SetTextI18n")
class HomeFragment : Fragment() {
    private val TAG = "FragmentHomeBinding"
    private var _homeBinding: FragmentHomeBinding? = null
    private val homeBinding get() = _homeBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            setSpinnerStrategy(layoutInflater.context)
            createListPerformanceEvaluation(layoutInflater.context)
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
                            setSpinnerDepartment(context, strategyId)
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
                            val departmentIds = if (p2 == 0 || strategyId == Optional.Absent) {
                                Optional.Absent
                            } else {
                                Optional.present(listOf(allDepartments[p2].id))
                            }
                            createChartOverview(strategyId, departmentIds)
                            createChartPerformance(strategyId, departmentIds)
                            createChartContributor(strategyId, departmentIds)
                            createChartSelfAssessment(strategyId, departmentIds)
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
        val departments =  Overview.shard.getAllDepartments(strategyId = strategyId)
        var arrDepartments = departments?.getAllDepartments?.toMutableList()
        if (strategyId == Optional.Absent) {
            val mapDepartment = mutableMapOf<String, GetAllDepartmentsQuery.GetAllDepartment>()
            arrDepartments?.forEach { getAllDepartment ->
                mapDepartment[getAllDepartment.name] = getAllDepartment
            }
            arrDepartments = mapDepartment.values.toMutableList()
            mapDepartment.clear()
        }
        arrDepartments?.add(0, GetAllDepartmentsQuery.GetAllDepartment(0.0, "Select Department", null))
        return arrDepartments
    }
    private fun createChartOverview(strategyId: Optional<Double?> = Optional.Absent, departmentIds: Optional<List<Double>?> = Optional.Absent)  {
        CoroutineScope(Dispatchers.IO).launch {
            val overallProgress = Overview.shard.getOverallProgress(departmentIds, strategyId)
            Log.d(TAG, "${overallProgress?.overallProgress}")
            val data = overallProgress?.overallProgress
            if (data != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val entries = arrayListOf(
                        PieEntry(data.percentComplete.toFloat(), "Complete"),
                        PieEntry((100 - data.percentComplete).toFloat(), "All")
                    )
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.setColors(Color.CYAN, Color.LTGRAY)
                    pieDataSet.isVisible = true
                    pieDataSet.valueTextSize = 0f
                    homeBinding.tvOverviewProgress.text = "${data.complete.toInt()} of ${data.overall.toInt()} Completed"
                    homeBinding.tvOverviewPercent.text = "Status ${data.percentComplete.toInt()}% Completed"

                    homeBinding.chartOverallProgress.setDrawEntryLabels(false)
                    homeBinding.chartOverallProgress.description.isEnabled = false
                    homeBinding.chartOverallProgress.data = PieData(pieDataSet)
                    homeBinding.chartOverallProgress.holeRadius = (homeBinding.chartOverallProgress.width * 0.13).toFloat()
                    homeBinding.chartOverallProgress.centerText = "${data.percentComplete}%"
                    homeBinding.chartOverallProgress.invalidate()
                }
            }
        }
    }
    private fun createChartPerformance(strategyId: Optional<Double?> = Optional.Absent, departmentIds: Optional<List<Double>?> = Optional.Absent)  {
        CoroutineScope(Dispatchers.IO).launch {
            val performanceEvaluation = Overview.shard.getPerformanceEvaluation(departmentIds, strategyId)
            val data = performanceEvaluation?.performanceEvaluation
            if (data != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val entries = arrayListOf(
                        PieEntry(data.percentComplete.toFloat(), "Complete"),
                        PieEntry((100 - data.percentComplete).toFloat(), "All")
                    )
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.setColors(Color.BLUE, Color.LTGRAY)
                    pieDataSet.isVisible = true
                    pieDataSet.valueTextSize = 0f
                    homeBinding.tvPerformanceProgress.text = "${data.complete.toInt()} of ${data.overall.toInt()} Completed"
                    homeBinding.tvPerformancePercent.text = "Status ${data.percentComplete.toInt()}% Completed"

                    homeBinding.chartPerformanceEvaluation.setDrawEntryLabels(false)
                    homeBinding.chartPerformanceEvaluation.description.isEnabled = false
                    homeBinding.chartPerformanceEvaluation.data = PieData(pieDataSet)
                    homeBinding.chartPerformanceEvaluation.holeRadius = (homeBinding.chartOverallProgress.width * 0.13).toFloat()
                    homeBinding.chartPerformanceEvaluation.centerText = "${data.percentComplete}%"
                    homeBinding.chartPerformanceEvaluation.invalidate()
                }
            }
        }
    }
    private fun createChartContributor(strategyId: Optional<Double?> = Optional.Absent, departmentIds: Optional<List<Double>?> = Optional.Absent)  {
        CoroutineScope(Dispatchers.IO).launch {
            val listContributors = Overview.shard.getListContributor(departmentIds, strategyId)
            val data = listContributors?.listContributors
            if (data != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val entries = arrayListOf(
                        PieEntry(data.percentComplete.toFloat(), "Complete"),
                        PieEntry((100 - data.percentComplete).toFloat(), "All")
                    )
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.setColors(Color.GREEN, Color.LTGRAY)
                    pieDataSet.isVisible = true
                    pieDataSet.valueTextSize = 0f
                    homeBinding.tvContributorProgress.text = "${data.complete.toInt()} of ${data.overall.toInt()} Completed"
                    homeBinding.tvContributorPercent.text = "Status ${data.percentComplete.toInt()}% Completed"

                    homeBinding.chartListContributor.setDrawEntryLabels(false)
                    homeBinding.chartListContributor.description.isEnabled = false
                    homeBinding.chartListContributor.data = PieData(pieDataSet)
                    homeBinding.chartListContributor.holeRadius = (homeBinding.chartOverallProgress.width * 0.13).toFloat()
                    homeBinding.chartListContributor.centerText = "${data.percentComplete}%"
                    homeBinding.chartListContributor.invalidate()
                }
            }
        }
    }
    private fun createChartSelfAssessment(strategyId: Optional<Double?> = Optional.Absent, departmentIds: Optional<List<Double>?> = Optional.Absent)  {
        CoroutineScope(Dispatchers.IO).launch {
            val selfAssessments = Overview.shard.getSelfAssessment(departmentIds, strategyId)
            val data = selfAssessments?.selfAssessments
            if (data != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val entries = arrayListOf(
                        PieEntry(data.percentComplete.toFloat(), "Complete"),
                        PieEntry((100 - data.percentComplete).toFloat(), "All")
                    )
                    val pieDataSet = PieDataSet(entries, "")
                    pieDataSet.setColors(Color.MAGENTA, Color.LTGRAY)
                    pieDataSet.isVisible = true
                    pieDataSet.valueTextSize = 0f
                    homeBinding.tvSelfAssessmentProgress.text = "${data.complete.toInt()} of ${data.overall.toInt()} Completed"
                    homeBinding.tvSelfAssessmentPercent.text = "Status ${data.percentComplete.toInt()}% Completed"

                    homeBinding.chartSelfAssessment.setDrawEntryLabels(false)
                    homeBinding.chartSelfAssessment.description.isEnabled = false
                    homeBinding.chartSelfAssessment.data = PieData(pieDataSet)
                    homeBinding.chartSelfAssessment.holeRadius = (homeBinding.chartOverallProgress.width * 0.13).toFloat()
                    homeBinding.chartSelfAssessment.centerText = "${data.percentComplete}%"
                    homeBinding.chartSelfAssessment.invalidate()
                }
            }
        }
    }
    private suspend fun createListPerformanceEvaluation(context: Context) {
        withContext(Dispatchers.IO) {
            val listPerformanceEvaluation = Overview.shard.getListPerformanceEvaluations()
            if (listPerformanceEvaluation?.getListPerformanceEvaluations != null) {
                CoroutineScope(Dispatchers.Main).launch {
                    val adapter = ListPerformanceEvaluationAdapter(context, listPerformanceEvaluation.getListPerformanceEvaluations)
                    homeBinding.rcvPerformanceEvaluations.layoutManager = LinearLayoutManager(context)
                    homeBinding.rcvPerformanceEvaluations.adapter = adapter
                }
            }
        }
    }
}
