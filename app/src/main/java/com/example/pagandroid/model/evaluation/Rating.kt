package com.example.pagandroid.model.evaluation

data class Rating(
    val median: Double,
    val strDev: Double,
    val nrmRating: Double,
    val nomalize: List<Double?>?
)
