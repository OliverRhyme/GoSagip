package dev.rhyme.gosagip.data

import dev.rhyme.gosagip.data.model.Ambulance
import dev.rhyme.gosagip.data.model.Rider
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiServices {

    @FormUrlEncoded
    @POST("ambulance/login")
    suspend fun ambulanceLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Ambulance

    @FormUrlEncoded
    @POST("rider/login")
    suspend fun riderLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): Rider

    @FormUrlEncoded
    @POST("rider/register")
    suspend fun riderRegister(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("Name") name: String,
        @Field("Address") address: String,
        @Field("plateNumber") plateNumber: String,
        @Field("emergencyContact") emergencyContact: String,
        @Field("emergencyContactNumber") emergencyContactNumber: String,
        @Field("bloodType") bloodType: String,
        @Field("deviceID") deviceId: String,
    ): Rider
}