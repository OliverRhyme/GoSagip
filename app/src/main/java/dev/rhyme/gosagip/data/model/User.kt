package dev.rhyme.gosagip.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import dev.rhyme.gosagip.data.utils.BooleanType

sealed class User(val type: UserType) {
    abstract val id: String
}

@JsonClass(generateAdapter = true)
data class Rider(
    @Json(name = "riderID")
    override val id: String,
    @Json(name = "Name")
    val name: String,
    @Json(name = "username")
    val username: String? = null,
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
    @BooleanType
    @Json(name = "hasBackride")
    val hasBackRide: Boolean
) : User(UserType.RIDER)

@JsonClass(generateAdapter = true)
data class Ambulance(
    @Json(name = "ambulanceID")
    override val id: String,
    @Json(name = "username")
    val username: String,
    @Json(name = "Name")
    val name: String
) : User(UserType.AMBULANCE)

enum class UserType {
    AMBULANCE,
    RIDER
}