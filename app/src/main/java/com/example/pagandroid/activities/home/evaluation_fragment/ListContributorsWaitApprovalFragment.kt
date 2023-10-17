package com.example.pagandroid.activities.home.evaluation_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pagandroid.activities.home.evaluation_fragment.adapter.StickyHeaderAdapter
import com.example.pagandroid.databinding.FragmentListContributorsWaitApprovalBinding
import com.example.pagandroid.model.LOCsWaitApproval.Header
import com.example.pagandroid.model.LOCsWaitApproval.ILOCsWaitApproval
import com.example.pagandroid.service.LOCsWaitApproval.LOCsWaitApprovalService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListContributorsWaitApprovalFragment : Fragment() {
    private var _contributorsWaitApprovalBinding: FragmentListContributorsWaitApprovalBinding? =
        null
    private val binding get() = _contributorsWaitApprovalBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _contributorsWaitApprovalBinding =
            FragmentListContributorsWaitApprovalBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            val data = getLOCsWaitApproval()
            CoroutineScope(Dispatchers.Main).launch {
                val manager = LinearLayoutManager(inflater.context)
                val adapter = StickyHeaderAdapter(data)
                binding.rcvContainerListContributor.layoutManager = manager
                binding.rcvContainerListContributor.adapter = adapter
                binding.rcvContainerListContributor.addItemDecoration(
                    DividerItemDecoration(
                        this@ListContributorsWaitApprovalFragment.context,
                        DividerItemDecoration.VERTICAL
                    )
                )
                binding.rcvContainerListContributor.addOnScrollListener(RcvStickyScroll(
                    data,
                    binding.tvStickyHeader,
                    manager
                ))
                binding.tvStickyHeader.text = (data[0] as Header).title
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _contributorsWaitApprovalBinding = null
    }

    private suspend fun getLOCsWaitApproval(): List<ILOCsWaitApproval> {
        return LOCsWaitApprovalService.shared.mapLOCsWaitApproval()
    }

    internal class RcvStickyScroll(
        val list: List<ILOCsWaitApproval>,
        private val tvHeader: TextView,
        private val rvLayoutManager: LinearLayoutManager
    ) : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            val position = rvLayoutManager.findFirstCompletelyVisibleItemPosition()
            val index = if (position < 0) {
                0
            } else if (position < 5) {
                position
            } else {
                4
            }
            val headerIndex = if (index == 3) {
                3
            } else {
                0
            }
            when (list[index]) {
                is Header -> tvHeader.visibility = View.GONE
                else -> {
                    tvHeader.visibility = View.VISIBLE
                    val title =
                        (list[headerIndex] as Header).title
                    tvHeader.text = title
                }
            }
        }
    }
}