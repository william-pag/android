package com.example.pagandroid.model.evaluation

data class EvaluationTypeAndQuestions(
    val id: Int,
    val name: String,
    val questions: MutableList<Question>
)