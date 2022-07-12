package dev.rhyme.gosagip.data.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    val locationUpdates: Flow<Location>
}