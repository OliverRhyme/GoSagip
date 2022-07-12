package dev.rhyme.gosagip.ui.pages

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rhyme.gosagip.data.AuthRepository
import dev.rhyme.gosagip.data.model.UserType
import dev.rhyme.gosagip.ui.pages.destinations.BackRidePageDestination
import dev.rhyme.gosagip.ui.pages.destinations.ChooserPageDestination
import dev.rhyme.gosagip.ui.pages.destinations.MonitoringPageDestination
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@RootNavGraph(start = true)
@Destination
@Composable
fun AuthRouterPage(
    navigator: DestinationsNavigator,
    viewModel: RouterViewModel = hiltViewModel()
) {
    val destination by viewModel.routeDestination.collectAsState()

    when (destination) {
        RouterViewModel.RouteDestination.INITIAL -> {}
        RouterViewModel.RouteDestination.LOGGED_IN_RIDER -> {
            navigator.navigate(BackRidePageDestination) {
                popUpTo(NavGraphs.root.route)
            }
        }
        RouterViewModel.RouteDestination.LOGGED_IN_AMBULANCE -> {
            navigator.navigate(MonitoringPageDestination) {
                popUpTo(NavGraphs.root.route)
            }
        }
        RouterViewModel.RouteDestination.LOGGED_OUT -> {
            navigator.navigate(ChooserPageDestination) {
                popUpTo(NavGraphs.root.route)
            }
        }
    }
}

@HiltViewModel
class RouterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    val routeDestination: StateFlow<RouteDestination> by lazy {
        authRepository.authState.map { user ->
            when (user?.type) {
                UserType.AMBULANCE -> RouteDestination.LOGGED_IN_AMBULANCE
                UserType.RIDER -> RouteDestination.LOGGED_IN_RIDER
                null -> RouteDestination.LOGGED_OUT
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), RouteDestination.INITIAL)
    }

    enum class RouteDestination {
        INITIAL,
        LOGGED_IN_RIDER,
        LOGGED_IN_AMBULANCE,
        LOGGED_OUT
    }
}
