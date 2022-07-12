package dev.rhyme.gosagip.data

import dev.rhyme.gosagip.data.model.Ambulance
import dev.rhyme.gosagip.data.model.Event
import dev.rhyme.gosagip.data.model.Rider
import retrofit2.http.*

interface ApiServices {

    @FormUrlEncoded
    @POST("ambulance/login")
    suspend fun ambulanceLogin(
        @Field("username") username: String,
        @Field("password") password: String
    ): List<Ambulance>

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
    ): List<Rider>

    @FormUrlEncoded
    @POST("rider/{id}")
    suspend fun updateRider(
        @Path("id") id: String,
        @Field("username") username: String,
        @Field("Name") name: String,
        @Field("Address") address: String,
        @Field("plateNumber") plateNumber: String,
        @Field("emergencyContact") emergencyContact: String,
        @Field("emergencyContactNumber") emergencyContactNumber: String,
        @Field("bloodType") bloodType: String,
//        @Field("deviceID") deviceId: String,
    ): List<Rider>

    @FormUrlEncoded
    @POST("rider/{id}/backride")
    suspend fun setBackRideStatus(
        @Path("id") id: String,
        @Field("backride") hasBackRide: Int
    )

    @FormUrlEncoded
    @POST("events/{id}/respond")
    suspend fun respondToAccident(
        @Path("id") accidentId: String,
        @Field("ambulanceID") ambulanceId: String,
        @Field("status") status: String
    )

    @GET("events/accident")
    suspend fun getEvents(): List<Event>
}