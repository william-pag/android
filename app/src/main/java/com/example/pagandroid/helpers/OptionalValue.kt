package com.example.pagandroid.helpers

import com.apollographql.apollo3.api.Optional

class OptionalValue {
    companion object {
        val shared = OptionalValue()
    }

    fun mapOptional(value: Int): Optional<Double> {
        return if (value == 0) {
            Optional.Absent
        } else {
            Optional.present(value.toDouble())
        }
    }
}