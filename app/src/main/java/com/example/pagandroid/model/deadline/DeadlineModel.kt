package com.example.pagandroid.model.deadline

data class DeadlineModel(
    val id: Double,
    val name: String,
    val deadlineType: String,
    val datetime: String,
    val overdue: String,
)