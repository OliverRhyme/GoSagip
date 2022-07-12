package dev.rhyme.gosagip.data.location

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FusedLocationService @Inject constructor(
    private val fusedLocationProvider: FusedLocationProviderClient
) : LocationService {

    companion object {
        private val request = LocationRequest.create().apply {
            this.interval = 3000
            this.fastestInterval = 1000
            this.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    @SuppressLint("MissingPermission")
    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ]
    )
    override val locationUpdates: Flow<Location> = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val lastLocation = result.lastLocation
                trySend(lastLocation)
            }
        }

        fusedLocationProvider.requestLocationUpdates(request, callback, Looper.getMainLooper())

        awaitClose {
            fusedLocationProvider.removeLocationUpdates(callback)
        }
    }.shareIn(GlobalScope, SharingStarted.WhileSubscribed(1500), 1)
}