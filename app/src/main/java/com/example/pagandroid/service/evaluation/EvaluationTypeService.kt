package com.example.pagandroid.service.evaluation

import com.apollographql.apollo3.api.Optional
import com.example.pagandroid.GetDistributionRatingsQuery
import com.example.pagandroid.dao.Evaluation
import com.example.pagandroid.helpers.OptionalValue
import com.example.pagandroid.model.evaluation.EvaluationTypeAndQuestions
import com.example.pagandroid.model.evaluation.Question
import com.example.pagandroid.model.evaluation.Rating
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
                    title = "All"
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

    suspend fun mapDataBarChart(typeId: Int, questionId: Int): Pair<List<GetDistributionRatingsQuery.Rating>?, Rating> {
        val type = OptionalValue.shared.mapOptional(typeId)
        val question = OptionalValue.shared.mapOptional(questionId)
        val data = Evaluation.shard.getDistributionRatings(type, question)
        val rating = Rating(
            median = data?.getDistributionRatings?.mean ?: 0.0,
            strDev = data?.getDistributionRatings?.stdDev ?: 0.0,
            nrmRating = data?.getDistributionRatings?.total ?: 0.0,
            nomalize = data?.getDistributionRatings?.ratings?.map { rating ->
                rating.normalize
            }
        )

        return Pair(data?.getDistributionRatings?.ratings, rating)
    }

    fun mapEntries(ratings: List<GetDistributionRatingsQuery.Rating>?): Pair<MutableList<BarEntry>, Int> {
        var total = 0
        val entries = mutableListOf<BarEntry>()
        ratings?.forEach { rating ->
            total += rating.entries.toInt()
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

        return Pair(entries, total)
    }
}
