package com.example.pagandroid.activities.home.evaluation_fragment.wait_approval.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TableRow
import android.widget.TextView
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
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
                holder.binding.chartWaitingApproval.holeRadius =
                    (holder.binding.chartWaitingApproval.width * 0.15).toFloat()
                holder.binding.chartWaitingApproval.centerText = "${data.percent}%"
                holder.binding.chartWaitingApproval.invalidate()
            }

            is SecondViewHolder -> {
                val adapter =
                    UserStatusAdapter((list[position] as UserWaitApproval).users) { inflater, viewGroup, attachToRoot ->
                        ItemContributorStatusBinding.inflate(inflater, viewGroup, attachToRoot)
                    }
                holder.binding.rcvListContributorStatus.layoutManager =
                    LinearLayoutManager(holder.binding.root.context)
                holder.binding.rcvListContributorStatus.adapter = adapter
                holder.binding.rcvListContributorStatus.addOnItemTouchListener(object :
                    RecyclerView.OnItemTouchListener {
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

                    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
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
                    holder.binding.spinnerForm.onItemSelectedListener =
                        object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(
                                p0: AdapterView<*>?,
                                p1: View?,
                                p2: Int,
                                p3: Long
                            ) {
                                setSpinnerQuestion(
                                    holder.binding.root.context,
                                    data[p2].id,
                                    data[p2].questions,
                                    holder.binding
                                )
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
        return when (list[position]) {
            is Header -> ViewType.Header.ordinal
            is Percentage -> ViewType.FirstContent.ordinal
            is UserWaitApproval -> ViewType.SecondContent.ordinal
            is EvaluationTypeAndQuestion -> ViewType.ThirdContent.ordinal
            else -> -1
        }
    }

    private fun setSpinnerQuestion(
        context: Context,
        typeId: Int,
        questions: MutableList<Question>,
        binding: StatisticEvaluationQuestionBinding
    ) {
        val dropdownEvaluationAdapter = DropdownEvaluationAdapter(
            context,
            R.layout.item_evaluation_dropdown_selected,
            questions
        )
        binding.spinnerQuestion.adapter = dropdownEvaluationAdapter
        binding.spinnerQuestion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val data =
                            EvaluationTypeService.shared.mapDataBarChart(typeId, questions[p2].id)
                        CoroutineScope(Dispatchers.Main).launch {
                            val (ratings, summary) = data
                            val (entries, total) = EvaluationTypeService.shared.mapEntries(ratings)

                            val dataSet = BarDataSet(entries, "Ratings")
                            val barData = BarData(dataSet)
                            binding.chartStatisticQuestion.data = barData
                            binding.chartStatisticQuestion.description.isEnabled = false
                            binding.chartStatisticQuestion.invalidate()
                            val context = binding.root.context
                            binding.tableStatistic.removeAllViews()
                            if (binding.tableStatistic.childCount == 0) {
                                val tableRow = TableRow(context)
                                for (i in 0..5) {
                                    val text = when (i) {
                                        0 -> "Rating"
                                        1 -> "# Entries"
                                        2 -> "% of Total"
                                        3 -> "Median"
                                        4 -> "Std Dev"
                                        5 -> "Nrm Rating"
                                        else -> ""
                                    }
                                    tableRow.addView(createTextView(context, text, true))
                                }
                                binding.tableStatistic.addView(tableRow)
                            }
                            for (k in 0..6) {
                                val tableRow = TableRow(context)
                                if (k < 6) {
                                    val rating = ratings?.get(k) ?: return@launch
                                    tableRow.addView(createTextView(
                                        context,
                                        "${rating.score?.toInt()}",
                                        true
                                    ))
                                    tableRow.addView(createTextView(
                                        context,
                                        "${rating.entries.toInt()}"
                                    ))
                                    tableRow.addView(createTextView(
                                        context, "${
                                            rating.percentage
                                        }%"
                                    ))
                                    when (k) {
                                        0 -> {
                                            tableRow.addView(createTextView(context, "${summary.median}"))
                                            tableRow.addView(createTextView(context, "${summary.strDev}"))
                                        }
                                        else -> {
                                            tableRow.addView(createTextView(context))
                                            tableRow.addView(createTextView(context))
                                        }
                                    }
                                    if (k in 1..5) {
                                        tableRow.addView(createTextView(
                                            context,
                                            "${summary.nomalize?.get(k)}"
                                        ))
                                    } else {
                                        tableRow.addView(createTextView(context))
                                    }
                                } else {
                                    tableRow.addView(createTextView(context, "Total", true))
                                    tableRow.addView(createTextView(context, "$total"))
                                    tableRow.addView(createTextView(context, "100%"))
                                    tableRow.addView(createTextView(context))
                                    tableRow.addView(createTextView(context))
                                    tableRow.addView(createTextView(context))
                                }

                                binding.tableStatistic.addView(tableRow)
                            }
                        }
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Log.d("EvaluationAdapter", "NothingSelected")
                }
            }
    }

    private fun createTextView(
        context: Context,
        text: String = "",
        isBold: Boolean = false
    ): TextView {
        val textView = TextView(context)
        textView.gravity = Gravity.CENTER
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        if (isBold) {
            textView.setTypeface(null, Typeface.BOLD)
        }
        textView.text = text
        val params = TableRow.LayoutParams(
            TableRow.LayoutParams.WRAP_CONTENT,
            TableRow.LayoutParams.WRAP_CONTENT,
            1f
        )
        params.bottomMargin = 30
        textView.layoutParams = params
        return textView
    }

}
