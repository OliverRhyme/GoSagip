package dev.rhyme.gosagip.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Rider(
    @Json(name = "riderId")
    val id: String,
    @Json(name = "Name")
    val name: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "Address")
    val address: String,
    @Json(name = "plateNumber")
    val plateNumber: String,
    @Json(name = "emergencyContact")
    val emergencyContact: String,
    @Json(name = "emergencyContactNumber")
    val emergencyContactNumber: String,
    @Json(name = "bloodType")
    val bloodType: BloodType,
    @Json(name = "hasBackride")
    val hasBackride: Int,
)
