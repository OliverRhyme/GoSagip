package dev.rhyme.gosagip.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.rhyme.gosagip.data.AccidentStatus
import dev.rhyme.gosagip.data.utils.DateTimestamp
import java.util.*

@JsonClass(generateAdapter = true)
data class Event(
    @Json(name = "eventID") val id: String,
    val rider: Rider,
    val latitude: Double,
    val longitude: Double,
    val status: AccidentStatus,
    @Json(name = "ambulanceID")
    val ambulanceId: String,
    @Json(name = "tStamp")
    @DateTimestamp
    val date: Date
)