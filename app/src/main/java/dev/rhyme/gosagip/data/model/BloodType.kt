package dev.rhyme.gosagip.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
enum class BloodType(val displayName: String) {
    @Json(name = "A+")
    A_POSITIVE("A+"),
    @Json(name = "A-")
    A_NEGATIVE("A-"),
    @Json(name = "B+")
    B_POSITIVE("B+"),
    @Json(name = "B-")
    B_NEGATIVE("B-"),
    @Json(name = "AB+")
    AB_POSITIVE("AB+"),
    @Json(name = "AB-")
    AB_NEGATIVE("AB-"),
    @Json(name = "0+")
    O_POSITIVE("0+"),
    @Json(name = "0-")
    O_NEGATIVE("0-");
}
