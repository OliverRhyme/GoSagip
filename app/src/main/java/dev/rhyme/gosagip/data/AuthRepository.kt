package dev.rhyme.gosagip.data

import com.fredporciuncula.flow.preferences.FlowSharedPreferences
import dev.rhyme.gosagip.data.model.BloodType
import dev.rhyme.gosagip.data.model.User
import dev.rhyme.gosagip.data.utils.MoshiUserSerializer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class AuthRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val flowSharedPreferences: FlowSharedPreferences,
    private val serializer: MoshiUserSerializer
) {
    private val userPrefs by lazy {
        flowSharedPreferences.getNullableObject("user", serializer, null)
    }

    @OptIn(DelicateCoroutinesApi::class)
    val authState: StateFlow<User?> by lazy {
        userPrefs.asFlow().stateIn(GlobalScope, SharingStarted.WhileSubscribed(), currentUser)
    }

    val currentUser: User?
        get() {
            return userPrefs.get()
        }

    suspend fun logout() {
        userPrefs.deleteAndCommit()
    }

    suspend fun modifyUser(user: User) {
        userPrefs.setAndCommit(user)
    }

    suspend fun riderLogin(
        username: String,
        password: String
    ) {
        val user = apiServices.riderLogin(username, password)
        userPrefs.setAndCommit(user)
    }

    suspend fun riderRegister(
        username: String,
        password: String,
        fullName: String,
        address: String,
        plateNumber: String,
        bloodType: BloodType,
        emergencyContact: String,
        emergencyContactNumber: String,
        deviceId: String
    ) {
        val user = apiServices.riderRegister(
            username = username,
            password = password,
            name = fullName,
            address = address,
            plateNumber = plateNumber,
            emergencyContact = emergencyContact,
            emergencyContactNumber = emergencyContactNumber,
            bloodType = bloodType.name,
            deviceId = deviceId
        ).single()
        userPrefs.setAndCommit(user)
    }

    suspend fun updateRider(
        id: String,
        username: String,
        fullName: String,
        address: String,
        plateNumber: String,
        bloodType: BloodType,
        emergencyContact: String,
        emergencyContactNumber: String
    ) {
        val user = apiServices.updateRider(
            id = id,
            username = username,
            name = fullName,
            address = address,
            plateNumber = plateNumber,
            emergencyContact = emergencyContact,
            emergencyContactNumber = emergencyContactNumber,
            bloodType = bloodType.name
        ).single()
        userPrefs.setAndCommit(user)
    }

    suspend fun ambulanceLogin(
        username: String,
        password: String
    ) {
        val user = apiServices.ambulanceLogin(username, password).single()
        userPrefs.setAndCommit(user)
    }
}