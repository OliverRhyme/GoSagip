package dev.rhyme.gosagip.ui.pages.monitoring

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApi
import com.google.maps.android.compose.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dev.rhyme.gosagip.data.AccidentStatus
import dev.rhyme.gosagip.data.model.Event
import dev.rhyme.gosagip.data.model.Rider
import dev.rhyme.gosagip.ui.pages.CommonPage
import dev.rhyme.gosagip.ui.pages.NavGraphs
import dev.rhyme.gosagip.ui.pages.destinations.ChooserPageDestination
import dev.rhyme.gosagip.utils.LocalGeoApiContext
import dev.rhyme.gosagip.utils.awaitCoroutine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class)
@Destination
@Composable
fun MonitoringPage(
    navigator: DestinationsNavigator,
    viewModel: MonitoringViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    CommonPage(
        icon = {
            IconButton(
                onClick = {
                    scope.launch {
                        if (viewModel.logout()) {
                            navigator.navigate(ChooserPageDestination) {
                                popUpTo(NavGraphs.root.route)
                            }
                        }
                    }
                }
            ) {
                Icon(Icons.Filled.ExitToApp, contentDescription = "Logout")
            }
        }
    ) {
        val currentUser by viewModel.currentUser.collectAsState()

        var selectedEvent: Event? by remember {
            mutableStateOf(null)
        }

        var completedRider: Rider? by remember {
            mutableStateOf(null)
        }

        completedRider?.let {
            Dialog(onDismissRequest = { completedRider = null }) {
                ResultDialog(onClose = { completedRider = null }) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text ="Rescue Completed!", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text ="Please contact relatives at ${it.emergencyContactNumber}")
                    }
                }
            }
        }

        selectedEvent?.let {
            Dialog(onDismissRequest = { selectedEvent = null }) {
                ResultDialog(
                    title ="RIDER INFORMATION",
                    onClose = { selectedEvent = null }
                ) {
                    Column {
                        RiderInfo(
                            modifier = Modifier.padding(16.dp),
                            rider = it.rider
                        )

                        if (it.status == AccidentStatus.ACTIVE || it.ambulanceId == currentUser?.id) {
                            Button(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                onClick = {
                                    scope.launch {
                                        when (it.status) {
                                            AccidentStatus.ACTIVE -> {
                                                viewModel.respond(it.id, AccidentStatus.PENDING)
                                            }
                                            AccidentStatus.PENDING -> {
                                                viewModel.respond(it.id, AccidentStatus.COMPLETED)
                                                withContext(Dispatchers.Main) {
                                                    completedRider = it.rider
                                                }
                                            }
                                            AccidentStatus.COMPLETED -> {}
                                        }
                                        selectedEvent = null
                                    }
                                }
                            ) {
                                Text(
                                    text = when (it.status) {
                                        AccidentStatus.ACTIVE -> "Rescue"
                                        AccidentStatus.PENDING -> "Complete"
                                        AccidentStatus.COMPLETED -> {
                                            "Completed"
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        val events by viewModel.events.collectAsState()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            val locationPermissionsState = rememberMultiplePermissionsState(
                listOf(
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )

            if (locationPermissionsState.allPermissionsGranted) {
                Text(
                    text = "INCIDENT MONITORING",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                )
                val ncr = LatLng(14.6091, 121.0223)
                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(ncr, 10.5f)
                }

                Spacer(modifier = Modifier.height(16.dp))
                GoogleMap(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    cameraPositionState = cameraPositionState,
                    properties = MapProperties(
                        isMyLocationEnabled = true,
                    )
                ) {

                    val activeEvent by remember {
                        derivedStateOf {
                            events.firstOrNull { event ->
                                event.status == AccidentStatus.PENDING && event.ambulanceId == currentUser?.id
                            }
                        }
                    }

                    LaunchedEffect(activeEvent) {
                        // log
                        Log.d("MonitoringPage", "activeEvent: $activeEvent")
                    }

                    events.forEach { event ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(event.latitude, event.longitude),
                            ),
                            onClick = {
                                selectedEvent = event
                                true
                            }
                        )
                    }

                    activeEvent?.let {
                        val geoApiContext = LocalGeoApiContext.current

                       val path: List<LatLng>? by produceState<List<LatLng>?>(initialValue = null) {
                           val currentLocation = viewModel.getCurrentLocation()
                           val route = DirectionsApi
                               .newRequest(geoApiContext)
                               .origin(com.google.maps.model.LatLng(currentLocation.latitude, currentLocation.longitude))
                               .destination(com.google.maps.model.LatLng(it.latitude, it.longitude))
                               .awaitCoroutine()
                               .routes.single().overviewPolyline

                           value = route.decodePath().map {
                                 LatLng(it.lat, it.lng)
                           }
                       }

                        path?.let {
                            Polyline(
                                points = it,
                                color = Color(0xFF2EC4B6)
                            )
                        }
                    }
                }


                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 24.dp)
                        .padding(vertical = 24.dp),
                    text = "STATUS: LIVE",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.W600
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    val allPermissionsRevoked =
                        locationPermissionsState.permissions.size ==
                            locationPermissionsState.revokedPermissions.size

                    val textToShow = if (!allPermissionsRevoked) {
                        // If not all the permissions are revoked, it's because the user accepted the COARSE
                        // location permission, but not the FINE one.
                        "We need your precise location to show you the map."
                    } else if (locationPermissionsState.shouldShowRationale) {
                        // Both location permissions have been denied
                        "Getting your exact location is important for this app. " +
                            "Please grant us fine location."
                    } else {
                        // First time the user sees this feature or the user doesn't want to be asked again
                        "This feature requires location permission"
                    }

                    val buttonText = if (!allPermissionsRevoked) {
                        "Allow precise location"
                    } else {
                        "Request permissions"
                    }

                    Text(text = textToShow)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { locationPermissionsState.launchMultiplePermissionRequest() }) {
                        Text(buttonText)
                    }
                }
            }
        }
    }
}

@Composable
fun RiderInfo(
    modifier: Modifier = Modifier,
    rider: Rider
) {
    Column(modifier = modifier) {
        Text(text = "Name:", fontWeight = FontWeight.Bold)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = rider.name
        )
        Text(text = "Address:", fontWeight = FontWeight.Bold)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = rider.address
        )
        Text(text = "Plate Number:", fontWeight = FontWeight.Bold)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = rider.plateNumber
        )
        Text(text = "Emergency Contact:", fontWeight = FontWeight.Bold)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = rider.emergencyContact
        )
        Text(text = "Emergency Contact Number:", fontWeight = FontWeight.Bold)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = rider.emergencyContactNumber
        )
        Text(text = "Blood Type:", fontWeight = FontWeight.Bold)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = rider.bloodType.displayName
        )
        Text(text = "Has Rider:", fontWeight = FontWeight.Bold)
        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = if (rider.hasBackRide) "Yes" else "No"
        )
    }

}

@Composable
fun ResultDialog(
    modifier: Modifier = Modifier,
    title: String = "",
    onClose: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = Color.White,
        contentColor = Color.Black
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Red)
            ) {
                CompositionLocalProvider(LocalContentColor provides Color.White) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = onClose
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                }
            }
            content()
        }
    }
}