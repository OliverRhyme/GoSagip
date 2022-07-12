package dev.rhyme.gosagip.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.maps.GeoApiContext
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint
import dev.rhyme.gosagip.ui.pages.NavGraphs
import dev.rhyme.gosagip.ui.theme.GoSagipTheme
import dev.rhyme.gosagip.utils.LocalGeoApiContext
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var geoApiContext: GeoApiContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            GoSagipTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                    color = MaterialTheme.colors.background
                ) {
                    CompositionLocalProvider(LocalGeoApiContext provides geoApiContext) {
                        DestinationsNavHost(
                            navGraph = NavGraphs.root,
                        )
                    }
                }
            }
        }
    }
}
