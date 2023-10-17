package com.example.pagandroid.model.LOCsWaitApproval

import com.example.pagandroid.model.evaluation.EvaluationTypeAndQuestions

data class EvaluationTypeAndQuestion(
    val data: MutableList<EvaluationTypeAndQuestions>
): ILOCsWaitApproval