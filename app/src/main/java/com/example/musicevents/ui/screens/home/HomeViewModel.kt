package com.example.musicevents.ui.screens.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.ui.screens.login.LoginViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val showLocationDisabledAlert: Boolean = false,
    val showLocationPermissionDeniedAlert: Boolean = false,
    val showLocationPermissionPermanentlyDeniedSnackbar: Boolean = false,
)
interface HomeActions{
    fun saveEvent(event: Event)

    suspend fun isSaved(userId: Int, eventId: String): Boolean

    fun setShowLocationDisabledAlert(show: Boolean)
    fun setShowLocationPermissionDeniedAlert(show: Boolean)
    fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean)
}

class HomeViewModel(
    private val eventsRepositories: EventsRepositories,
    private val loginViewModel: LoginViewModel
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    val actions = object: HomeActions{
        override fun saveEvent(event: Event) {
            viewModelScope.launch {
                loginViewModel.userLogged.id?.let { eventsRepositories.userSaveEvent(it, event) }
            }
        }

        override suspend fun isSaved(userId: Int, eventId: String): Boolean {
            return false
        }

        override fun setShowLocationDisabledAlert(show: Boolean) =
            _state.update { it.copy(showLocationDisabledAlert = show) }

        override fun setShowLocationPermissionDeniedAlert(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDeniedAlert = show) }

        override fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean) =
            _state.update { it.copy(showLocationPermissionPermanentlyDeniedSnackbar = show) }
    }
}