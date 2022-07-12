package dev.rhyme.gosagip.ui.pages.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rhyme.gosagip.data.AuthRepository
import dev.rhyme.gosagip.data.model.BloodType
import dev.rhyme.gosagip.data.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _errorState = MutableSharedFlow<String>()
    val errorState: SharedFlow<String> = _errorState.asSharedFlow()

    val currentUser: StateFlow<User> by lazy {
        authRepository.authState
            .filterNotNull()
            .filterIsInstance<User>()
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = 500L
                ), authRepository.authState.value!!
            )
    }

    suspend fun loginRider(
        username: String,
        password: String
    ): Boolean {

        return withContext(viewModelScope.coroutineContext) {
            try {
                if (username.isEmpty() || password.isEmpty()) {
                    throw IllegalArgumentException("Username or password is empty")
                }
                authRepository.riderLogin(username, password)
                true
            } catch (e: Exception) {
                _errorState.emit(e.message.orEmpty())
                false
            }
        }
    }

    suspend fun updateRider(
        username: String,
        fullName: String,
        address: String,
        plateNumber: String,
        bloodType: BloodType?,
        emergencyContact: String,
        emergencyContactNumber: String
    ): Boolean {

        return withContext(viewModelScope.coroutineContext) {
            try {
                if (username.isEmpty() || fullName.isEmpty() || address.isEmpty() || plateNumber.isEmpty() || emergencyContact.isEmpty() || emergencyContactNumber.isEmpty() || bloodType == null) {
                    throw IllegalArgumentException("All fields should be populated")
                }
                authRepository.updateRider(
                    currentUser.value.id,
                    username,
                    fullName,
                    address,
                    plateNumber,
                    bloodType,
                    emergencyContact,
                    emergencyContactNumber
                )
                true
            } catch (e: Exception) {
                _errorState.emit(e.message.orEmpty())
                false
            }
        }
    }

    suspend fun registerRider(
        username: String,
        password: String,
        fullName: String,
        address: String,
        plateNumber: String,
        bloodType: BloodType?,
        emergencyContact: String,
        emergencyContactNumber: String,
        deviceId: String
    ): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            try {
                if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || address.isEmpty() || plateNumber.isEmpty() || emergencyContact.isEmpty() || emergencyContactNumber.isEmpty() || deviceId.isEmpty() || bloodType == null) {
                    throw IllegalArgumentException("Please fill all the fields")
                }
                authRepository.riderRegister(
                    username = username,
                    password = password,
                    fullName = fullName,
                    address = address,
                    plateNumber = plateNumber,
                    bloodType = bloodType,
                    emergencyContact = emergencyContact,
                    emergencyContactNumber = emergencyContactNumber,
                    deviceId = deviceId
                )
                true
            } catch (e: Exception) {
                _errorState.emit(e.message.orEmpty())
                false
            }
        }
    }

    suspend fun loginAmbulance(
        username: String,
        password: String
    ): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            try {
                if (username.isEmpty() || password.isEmpty()) {
                    throw IllegalArgumentException("Username or password is empty")
                }
                authRepository.ambulanceLogin(username, password)
                true
            } catch (e: Exception) {
                _errorState.emit(e.message.orEmpty())
                false
            }
        }
    }
}
