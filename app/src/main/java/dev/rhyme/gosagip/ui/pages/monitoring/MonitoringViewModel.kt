package dev.rhyme.gosagip.ui.pages.monitoring

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rhyme.gosagip.data.AccidentStatus
import dev.rhyme.gosagip.data.AuthRepository
import dev.rhyme.gosagip.data.GoSagipRepository
import dev.rhyme.gosagip.data.location.LocationService
import dev.rhyme.gosagip.data.model.Ambulance
import dev.rhyme.gosagip.data.model.Event
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MonitoringViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val goSagipRepository: GoSagipRepository,
    private val locationService: LocationService
) : ViewModel() {

    suspend fun logout(): Boolean {
        return withContext(viewModelScope.coroutineContext) {
            try {
                authRepository.logout()
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun respond(accidentId: String, response: AccidentStatus) {
        withContext(viewModelScope.coroutineContext) {
            goSagipRepository.respondToAccident(
                accidentId = accidentId,
                ambulanceId = currentUser.value!!.id,
                response = response
            )
        }
    }

    val events: StateFlow<List<Event>> by lazy {
        goSagipRepository.getEvents().stateIn(
            viewModelScope, SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 500L
            ), emptyList()
        )
    }

    val currentUser: StateFlow<Ambulance?> by lazy {
        authRepository.authState
            .filterNotNull()
            .filterIsInstance<Ambulance>()
            .stateIn(
                viewModelScope, SharingStarted.WhileSubscribed(
                    stopTimeoutMillis = 500L
                ), authRepository.authState.value as? Ambulance
            )
    }

    suspend fun getCurrentLocation(): Location {
        return locationService.locationUpdates.first()
    }
}