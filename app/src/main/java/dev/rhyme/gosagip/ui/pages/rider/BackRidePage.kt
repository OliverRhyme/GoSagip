package dev.rhyme.gosagip.ui.pages.rider

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rhyme.gosagip.data.AuthRepository
import dev.rhyme.gosagip.data.GoSagipRepository
import dev.rhyme.gosagip.data.model.Rider
import dev.rhyme.gosagip.ui.pages.CommonPage
import dev.rhyme.gosagip.ui.pages.NavGraphs
import dev.rhyme.gosagip.ui.pages.destinations.ChooserPageDestination
import dev.rhyme.gosagip.ui.pages.destinations.ModifyDetailsPageDestination
import dev.rhyme.gosagip.ui.theme.GoSagipTheme
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BackRideViewModel @Inject constructor(
    private val goSagipRepository: GoSagipRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    val hasBackRide: StateFlow<Boolean?> = authRepository.authState
        .filterNotNull()
        .filterIsInstance<Rider>()
        .map { it.hasBackRide }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun setBackRide(hasBackRide: Boolean) {
        viewModelScope.launch {
            goSagipRepository.setHasBackRide(
                riderId = (authRepository.currentUser as Rider).id,
                hasBackRide = hasBackRide
            )
            authRepository.modifyUser(
                (authRepository.currentUser as Rider).copy(
                    hasBackRide = hasBackRide
                )
            )
        }
    }

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
}

@Destination
@Composable
fun BackRidePage(
    navigator: DestinationsNavigator,
    viewModel: BackRideViewModel = hiltViewModel()
) {

    val hasBackRide by viewModel.hasBackRide.collectAsState()

    val scope = rememberCoroutineScope()

    CommonPage(
        icon = {
            Row {

                IconButton(onClick = {
                    navigator.navigate(ModifyDetailsPageDestination)
                }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Edit Info")
                }
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
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Do you have a backride?",
                fontWeight = FontWeight.Bold,
                fontSize = 27.sp
            )
            Spacer(modifier = Modifier.height(64.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()

            ) {
                BackRideChooserButton(
                    modifier = Modifier.weight(1f),
                    text = "No",
                    selected = hasBackRide == false,
                    onSelect = {
                        viewModel.setBackRide(false)
                    }
                )
                BackRideChooserButton(
                    modifier = Modifier.weight(1f),
                    text = "Yes",
                    selected = hasBackRide == true,
                    onSelect = {
                        viewModel.setBackRide(true)
                    }
                )
            }
        }
    }
}

@Composable
private fun BackRideChooserButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelect: (Boolean) -> Unit
) {

    val selectedAlpha by animateFloatAsState(
        targetValue = if (selected) 1.0f else 0f,
        animationSpec = spring(
            stiffness = Spring.StiffnessLow,
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    0.3f to Color(0xFF011627),
                    0.9f to Color(0x800973C8)
                ),
                alpha = selectedAlpha
            )
            .clickable { onSelect(!selected) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 27.sp
        )
    }
}

@Preview
@Composable
fun BackRideChooserButtonPreview() {
    GoSagipTheme {
        Surface(color = MaterialTheme.colors.background) {
            BackRideChooserButton(
                text = "Yes",
                selected = true,
                onSelect = {}
            )
        }
    }
}

@Preview
@Composable
fun BackRidePagePreview() {
    GoSagipTheme {
        Surface(color = MaterialTheme.colors.background) {
            BackRidePage(
                navigator = EmptyDestinationsNavigator
            )
        }
    }
}