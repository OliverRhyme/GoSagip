package dev.rhyme.gosagip.utils

import androidx.compose.runtime.staticCompositionLocalOf
import com.google.maps.GeoApiContext

val LocalGeoApiContext = staticCompositionLocalOf<GeoApiContext> {
    throw IllegalStateException("No GeoApiContext")
}