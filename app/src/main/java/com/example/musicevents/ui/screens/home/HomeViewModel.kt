package com.example.musicevents.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicevents.data.remote.Genre
import com.example.musicevents.data.remote.JambaseSource
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.data.repositories.UserRepository
import com.example.musicevents.utils.InternetService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeState(
    val showLocationDisabledAlert: Boolean = false,
    val showLocationPermissionDeniedAlert: Boolean = false,
    val showLocationPermissionPermanentlyDeniedSnackbar: Boolean = false,
    val showNoInternetConnectivitySnackbar: Boolean = false,
    var genreList: List<Genre>
)
interface HomeActions{
    fun setShowLocationDisabledAlert(show: Boolean)
    fun setShowLocationPermissionDeniedAlert(show: Boolean)
    fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean)
    fun setShowNoInternetConnectivitySnackbar(show: Boolean)
    fun isOnline(): Boolean
    fun openWirelessSettings()
}

class HomeViewModel(
    private val internet: InternetService,
    private val jambaseApi: JambaseSource
): ViewModel() {

    private val _state = MutableStateFlow(HomeState(genreList = emptyList()))
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            if(internet.isOnline()){
                _state.value.genreList = jambaseApi.getAllGenres().genres
            }
        }
    }

    val actions = object: HomeActions{

        override fun setShowLocationDisabledAlert(show: Boolean) =
            _state.update { it.copy(showLocationDisabledAlert = show) }

        override fun setShowLocationPermissionDeniedAlert(show: Boolean) =
            _state.update { it.copy(showLocationPermissionDeniedAlert = show) }

        override fun setShowLocationPermissionPermanentlyDeniedSnackbar(show: Boolean) =
            _state.update { it.copy(showLocationPermissionPermanentlyDeniedSnackbar = show) }

        override fun setShowNoInternetConnectivitySnackbar(show: Boolean) =
            _state.update { it.copy(showNoInternetConnectivitySnackbar = show) }

        override fun isOnline(): Boolean {
            return internet.isOnline()
        }

        override fun openWirelessSettings() {
            internet.openWirelessSettings()
        }

    }
}