package dev.rhyme.gosagip.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ambulance(
    @Json(name = "ambulanceID")
    val id: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "Name")
    val name: String
)