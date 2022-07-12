package dev.rhyme.gosagip.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class AccidentStatus {
    ACTIVE, PENDING, COMPLETED
}