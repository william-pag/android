package com.example.pagandroid.activities.home.evaluation_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pagandroid.activities.home.evaluation_fragment.adapter.StickyHeaderAdapter
import com.example.pagandroid.databinding.FragmentListContributorsWaitApprovalBinding
import com.example.pagandroid.model.LOCsWaitApproval.ILOCsWaitApproval
import com.example.pagandroid.service.LOCsWaitApproval.LOCsWaitApprovalService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListContributorsWaitApprovalFragment : Fragment() {
    private var _contributorsWaitApprovalBinding: FragmentListContributorsWaitApprovalBinding? = null
    private val binding get() = _contributorsWaitApprovalBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _contributorsWaitApprovalBinding = FragmentListContributorsWaitApprovalBinding.inflate(inflater, container, false)
        CoroutineScope(Dispatchers.IO).launch {
            val data = getLOCsWaitApproval()
            CoroutineScope(Dispatchers.Main).launch {
                val adapter = StickyHeaderAdapter(data)
                binding.rcvContainerListContributor.layoutManager = LinearLayoutManager(inflater.context)
                binding.rcvContainerListContributor.adapter = adapter
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
}