package dev.rhyme.gosagip.data

import android.util.Log
import dev.rhyme.gosagip.data.model.Event
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Singleton
class GoSagipRepository @Inject constructor(
    private val apiServices: ApiServices
) {

    private val poller: MutableSharedFlow<Unit> = MutableSharedFlow()

    suspend fun setHasBackRide(
        riderId: String,
        hasBackRide: Boolean
    ) {
        apiServices.setBackRideStatus(
            id = riderId,
            hasBackRide = if (hasBackRide) 1 else 0
        )
    }

    suspend fun respondToAccident(
        accidentId: String,
        ambulanceId: String,
        response: AccidentStatus
    ) {
        apiServices.respondToAccident(
            accidentId = accidentId,
            ambulanceId = ambulanceId,
            status = response.name
        )
        poller.emit(Unit)
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getEvents(
        interval: Duration = 5.seconds
    ): Flow<List<Event>> = channelFlow {
        withContext(CoroutineExceptionHandler { coroutineContext, throwable ->
            Log.e("GoSagipRepository", "Error getting events", throwable)
        }) {
            launch {
                poller.collect {
                    send(apiServices.getEvents())
                }
            }
            launch {
                while (coroutineContext.isActive) {
                    val events = apiServices.getEvents()
                    send(events)
                    delay(interval)
                }
            }
        }
    }.distinctUntilChanged()
        .shareIn(GlobalScope, SharingStarted.WhileSubscribed(), 1)
}