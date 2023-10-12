package com.example.pagandroid.activities.home.bottom_fragment.dropdown

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.pagandroid.GetAllDepartmentsQuery
import com.example.pagandroid.GetAllStrategiesQuery
import com.example.pagandroid.GetAllUserNameQuery
import com.example.pagandroid.databinding.ItemEvaluationDropdownBinding
import com.example.pagandroid.databinding.ItemEvaluationDropdownSelectedBinding
class DropdownEvaluationAdapter<T>(
    private val context: Context,
    private val resource: Int,
    arrObject: MutableList<T>
): ArrayAdapter<T>(context, resource, arrObject) {
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
        if (result is GetAllStrategiesQuery.GetAllStrategy) {
            dropdownSelectedBinding.tvSelectEvaluationDropdown.text = result?.name
        } else if (result is GetAllDepartmentsQuery.GetAllDepartment) {
            dropdownSelectedBinding.tvSelectEvaluationDropdown.text = result?.name
        } else if (result is GetAllUserNameQuery.GetAllUser) {
            dropdownSelectedBinding.tvSelectEvaluationDropdown.text = result?.name
        }
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
        if (result is GetAllStrategiesQuery.GetAllStrategy) {
            dropdownBinding.tvTitleDropdown.text = result?.name
        } else if (result is GetAllDepartmentsQuery.GetAllDepartment) {
            dropdownBinding.tvTitleDropdown.text = result?.name
        } else if (result is GetAllUserNameQuery.GetAllUser) {
            dropdownBinding.tvTitleDropdown.text = result?.name
        }
        if (position == 0) {
            dropdownBinding.tvTitleDropdown.setTextColor(Color.GRAY)
        }
        return row
    }
}
