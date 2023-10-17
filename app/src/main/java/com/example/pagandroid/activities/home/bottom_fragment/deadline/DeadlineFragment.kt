package com.example.pagandroid.activities.home.bottom_fragment.deadline

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.R
import com.example.pagandroid.activities.adapter.DropdownEvaluationAdapter
import com.example.pagandroid.activities.home.bottom_fragment.dropdown.IGetDepartment
import com.example.pagandroid.activities.home.bottom_fragment.dropdown.IGetStrategy
import com.example.pagandroid.databinding.FragmentDeadlineBinding
import com.example.pagandroid.databinding.ItemDeadlineBinding
import com.example.pagandroid.service.deadline.DeadlineService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [DeadlineFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DeadlineFragment : Fragment(), IGetStrategy, IGetDepartment {
    private var _deadlineBinding: FragmentDeadlineBinding? = null
    private val deadlineBinding get() = _deadlineBinding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _deadlineBinding = FragmentDeadlineBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            setSpinnerStrategy(inflater.context)
        }
        return this.deadlineBinding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _deadlineBinding = null
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
                    deadlineBinding.spinnerStrategy.adapter = dropdownEvaluationAdapter
                    deadlineBinding.spinnerStrategy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                    deadlineBinding.spinnerDepartment.adapter = dropdownEvaluationAdapter
                    deadlineBinding.spinnerDepartment.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            val departmentId = if (p2 == 0 || strategyId == Optional.Absent) {
                                Optional.Absent
                            } else {
                                Optional.present(allDepartments[p2].id)
                            }
                            createListDeadline(context, strategyId, departmentId)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            Log.d("EvaluationAdapter", "NothingSelected")
                        }

                    }
                }
            }
        }
    }

    private fun createListDeadline(
        context: Context,
        strategyId: Optional<Double?> = Optional.Absent,
        departmentId: Optional<Double?> = Optional.Absent,
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val deadlines = DeadlineService.shared.getListDeadline(strategyId, departmentId)

            CoroutineScope(Dispatchers.Main).launch {
                val adapter = ListDeadlineAdapter(
                    deadlines
                ) { inflater, viewGroup, attachToRoot ->
                    ItemDeadlineBinding.inflate(inflater, viewGroup, attachToRoot)
                }

                deadlineBinding.rcvListDeadline.layoutManager = LinearLayoutManager(context)
                deadlineBinding.rcvListDeadline.adapter = adapter
            }
        }
    }
}
