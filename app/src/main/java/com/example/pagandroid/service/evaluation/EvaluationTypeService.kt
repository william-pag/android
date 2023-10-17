package com.example.pagandroid.service.evaluation

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.dao.Evaluation
import com.example.pagandroid.helpers.OptionalValue
import com.example.pagandroid.model.evaluation.EvaluationTypeAndQuestions
import com.example.pagandroid.model.evaluation.Question
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry

class EvaluationTypeService {
    companion object {
        val shared = EvaluationTypeService()
    }

    suspend fun mapEvaluationType(): MutableList<EvaluationTypeAndQuestions> {
        val evalTypeQuestion = mutableListOf<EvaluationTypeAndQuestions>(
            EvaluationTypeAndQuestions(
                id = 0,
                name = "All",
                mutableListOf(Question(
                    id = 0,
                    title = "Alll"
                ))
            )
        )
        val result = Evaluation.shard.getAllEvaluationTypes()
        result?.getAllEvaluationTypes?.forEach { evaluation ->
            val questions = mutableListOf<Question>(
                Question(
                    id = 0,
                    title = "All"
                )
            )
            evaluation.evaluationTypeQuestions.forEach { question ->
                if (question.isEvaluation == true && question.isOpenQuestion == false) {
                    questions.add(
                        Question(
                            id = question.id.toInt(),
                            title = question.title
                        )
                    )
                }
            }
            evalTypeQuestion.add(
                EvaluationTypeAndQuestions(
                    id = evaluation.id.toInt(),
                    name = evaluation.name,
                    questions
                )
            )
        }
        return evalTypeQuestion
    }

    suspend fun mapDataBarChart(typeId: Int, questionId: Int): MutableList<BarEntry> {
        val type = OptionalValue.shared.mapOptional(typeId)
        val question = OptionalValue.shared.mapOptional(questionId)
        val entries = mutableListOf<BarEntry>()
        val data = Evaluation.shard.getDistributionRatings(type, question)
        data?.getDistributionRatings?.ratings?.forEach { rating ->
            entries.add(
                BarEntry(
                    if (rating.score != null) {
                        rating.score.toFloat()
                    } else {
                        0F
                    },
                    rating.entries.toFloat()
                )
            )
        }
        return entries
    }
}