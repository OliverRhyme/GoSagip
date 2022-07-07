package dev.rhyme.gosagip.ui.pages.auth

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rhyme.gosagip.data.ApiServices
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val apiServices: ApiServices
): ViewModel() {

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState())
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    private val _actionState: MutableSharedFlow<ActionState> = MutableSharedFlow()
    val actionState: SharedFlow<ActionState> = _actionState.asSharedFlow()


    data class ViewState(
        val isLoading: Boolean = false,
    )

    sealed interface ActionState {
        object Success: ActionState
    }

    fun loginRider(
        username: String,
        password: String
    ) {
        _viewState.value = _viewState.value.copy(isLoading = true)
//        apiServices.loginRider(username, password)
//            .also {
//                _viewState.value = _viewState.value.copy(isLoading = false)
//            }
    }

    fun loginAmbulance(
        username: String,
        password: String
    ) {

    }
}
