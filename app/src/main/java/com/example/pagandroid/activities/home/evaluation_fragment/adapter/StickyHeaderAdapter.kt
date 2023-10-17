package com.example.pagandroid.activities.home.evaluation_fragment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagandroid.R
import com.example.pagandroid.activities.adapter.DropdownEvaluationAdapter
import com.example.pagandroid.databinding.FirstContentContributorBinding
import com.example.pagandroid.databinding.ItemContributorStatusBinding
import com.example.pagandroid.databinding.ItemHeaderContributorBinding
import com.example.pagandroid.databinding.RcvListStatusContributorBinding
import com.example.pagandroid.databinding.StatisticEvaluationQuestionBinding
import com.example.pagandroid.model.LOCsWaitApproval.EvaluationTypeAndQuestion
import com.example.pagandroid.model.LOCsWaitApproval.Header
import com.example.pagandroid.model.LOCsWaitApproval.ILOCsWaitApproval
import com.example.pagandroid.model.LOCsWaitApproval.Percentage
import com.example.pagandroid.model.LOCsWaitApproval.UserWaitApproval
import com.example.pagandroid.model.evaluation.Question
import com.example.pagandroid.service.evaluation.EvaluationTypeService
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Optional

class StickyHeaderAdapter(private val list: List<ILOCsWaitApproval>) :
    RecyclerView.Adapter<StickyHeaderAdapter.Companion.BaseViewHolder<*>>() {

    inner class HeaderViewHolder(val binding: ItemHeaderContributorBinding) :
        BaseViewHolder<ItemHeaderContributorBinding>(binding.root)

    inner class FirstViewHolder(val binding: FirstContentContributorBinding) :
        BaseViewHolder<FirstContentContributorBinding>(binding.root)

    inner class SecondViewHolder(val binding: RcvListStatusContributorBinding) :
        BaseViewHolder<RcvListStatusContributorBinding>(binding.root)
    inner class ThirdViewHolder(val binding: StatisticEvaluationQuestionBinding) :
        BaseViewHolder<StatisticEvaluationQuestionBinding>(binding.root)

    companion object {
        abstract class BaseViewHolder<T>(view: View) : RecyclerView.ViewHolder(view)
    }

    enum class ViewType {
        Header,
        FirstContent,
        SecondContent,
        ThirdContent
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            ViewType.Header.ordinal -> {
                val layout = ItemHeaderContributorBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                HeaderViewHolder(layout)
            }

            ViewType.FirstContent.ordinal -> {
                val layout = FirstContentContributorBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                FirstViewHolder(layout)
            }

            ViewType.SecondContent.ordinal -> {
                val layout = RcvListStatusContributorBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                SecondViewHolder(layout)
            }

            ViewType.ThirdContent.ordinal -> {
                val layout = StatisticEvaluationQuestionBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                ThirdViewHolder(layout)
            }

            else -> throw IllegalArgumentException("Not a layout")
        }
    }

    override fun getItemCount(): Int = list.size

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.binding.tvHeaderContributor.text = (list[position] as Header).title
            }
            is FirstViewHolder -> {
                val data = list[position] as Percentage
                holder.binding.tvPercentStatus.text = "Status ${data.percent}% Completed"
                val entries = arrayListOf(
                    PieEntry(data.percent.toFloat(), "Complete"),
                    PieEntry((100 - data.percent).toFloat(), "All")
                )
                val pieDataSet = PieDataSet(entries, "")
                pieDataSet.setColors(Color.CYAN, Color.LTGRAY)
                pieDataSet.isVisible = true
                pieDataSet.valueTextSize = 0f

                holder.binding.chartWaitingApproval.setDrawEntryLabels(false)
                holder.binding.chartWaitingApproval.description.isEnabled = false
                holder.binding.chartWaitingApproval.data = PieData(pieDataSet)
                holder.binding.chartWaitingApproval.holeRadius = (holder.binding.chartWaitingApproval.width * 0.15).toFloat()
                holder.binding.chartWaitingApproval.centerText = "${data.percent}%"
                holder.binding.chartWaitingApproval.invalidate()
            }
            is SecondViewHolder -> {
                val adapter = UserStatusAdapter((list[position] as UserWaitApproval).users) { inflater, viewGroup, attachToRoot ->
                    ItemContributorStatusBinding.inflate(inflater, viewGroup, attachToRoot)
                }
                holder.binding.rcvListContributorStatus.layoutManager = LinearLayoutManager(holder.binding.root.context)
                holder.binding.rcvListContributorStatus.adapter = adapter
                holder.binding.rcvListContributorStatus.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                        var initialTouchY = 0F;
                        val action = e.action
                        val y = e.y
                        when (action) {
                            MotionEvent.ACTION_DOWN -> {
                                // Save the initial touch position
                                initialTouchY = y
                                rv.parent.requestDisallowInterceptTouchEvent(true)
                            }
                            MotionEvent.ACTION_MOVE -> {
                                val dy = y - initialTouchY
                                if (rv.canScrollVertically(-1) && dy > 0 || rv.canScrollVertically(1) && dy < 0) {
                                    // InnerRecyclerView is being scrolled vertically, allow the outerRecyclerView to handle the touch event
                                    rv.parent.requestDisallowInterceptTouchEvent(false)
                                }
                            }
                        }
                        return false
                    }

                    override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}

                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) { }
                })
            }
            is ThirdViewHolder -> {
                CoroutineScope(Dispatchers.Main).launch {
                    val data = (list[position] as EvaluationTypeAndQuestion).data
                    val dropdownEvaluationAdapter = DropdownEvaluationAdapter(
                        holder.binding.root.context,
                        R.layout.item_evaluation_dropdown_selected,
                        data
                    )
                    holder.binding.spinnerForm.adapter = dropdownEvaluationAdapter
                    holder.binding.spinnerForm.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                            setSpinnerQuestion(holder.binding.root.context, data[p2].id, data[p2].questions, holder.binding)
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            Log.d("EvaluationAdapter", "NothingSelected")
                        }
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position]) {
            is Header -> ViewType.Header.ordinal
            is Percentage -> ViewType.FirstContent.ordinal
            is UserWaitApproval -> ViewType.SecondContent.ordinal
            is EvaluationTypeAndQuestion -> ViewType.ThirdContent.ordinal
            else -> -1
        }
    }

    private fun setSpinnerQuestion(context: Context, typeId: Int, questions: MutableList<Question>, binding:  StatisticEvaluationQuestionBinding) {
        val dropdownEvaluationAdapter = DropdownEvaluationAdapter(
            context,
            R.layout.item_evaluation_dropdown_selected,
            questions
        )
        binding.spinnerQuestion.adapter = dropdownEvaluationAdapter
        binding.spinnerQuestion.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                CoroutineScope(Dispatchers.IO).launch {
                    val entries = EvaluationTypeService.shared.mapDataBarChart(typeId, questions[p2].id)
                    CoroutineScope(Dispatchers.Main).launch {
                        val dataSet = BarDataSet(entries, "Ratings")
                        val barData = BarData(dataSet)
                        binding.chartStatisticQuestion.data = barData
                        binding.chartStatisticQuestion.description.isEnabled = false
                        binding.chartStatisticQuestion.invalidate()
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("EvaluationAdapter", "NothingSelected")
            }
        }
    }
}