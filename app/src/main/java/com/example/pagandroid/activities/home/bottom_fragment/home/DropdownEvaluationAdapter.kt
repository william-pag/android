package com.example.pagandroid.activities.home.bottom_fragment.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.pagandroid.GetAllDepartmentsQuery
import com.example.pagandroid.GetAllStrategiesQuery
import com.example.pagandroid.databinding.ItemEvaluationDropdownBinding
import com.example.pagandroid.databinding.ItemEvaluationDropdownSelectedBinding

class DropdownEvaluationAdapter(
    private val context: Context,
    private val resource: Int,
    arrObject: Array<out GetAllStrategiesQuery.Data?>
): ArrayAdapter<GetAllStrategiesQuery.Data>(context, resource, arrObject) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dropdownSelectedBinding: ItemEvaluationDropdownSelectedBinding
        var row = convertView
        if (row == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            dropdownSelectedBinding = ItemEvaluationDropdownSelectedBinding.inflate(inflater, parent, false)
            row = dropdownSelectedBinding.root
        } else {
            dropdownSelectedBinding = ItemEvaluationDropdownSelectedBinding.bind(row)
        }
        val result = this.getItem(position)
        dropdownSelectedBinding.tvSelectEvaluationDropdown.text = result?.getAllStrategies?.get(position)?.name ?: ""
        return row
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val dropdownBinding: ItemEvaluationDropdownBinding
        var row = convertView
        if (row == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            dropdownBinding = ItemEvaluationDropdownBinding.inflate(inflater, parent, false)
            row = dropdownBinding.root
        } else {
            dropdownBinding = ItemEvaluationDropdownBinding.bind(row)
        }
        val result = this.getItem(position)
        dropdownBinding.tvTitleDropdown.text = result?.getAllStrategies?.get(position)?.name ?: ""
        return row
    }
}
