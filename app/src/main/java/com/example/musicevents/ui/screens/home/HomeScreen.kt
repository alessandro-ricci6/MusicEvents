package com.example.musicevents.ui.screens.home

import android.Manifest
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.media.metrics.Event
import android.provider.Settings
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.musicevents.data.remote.EventApi
import com.example.musicevents.data.remote.JamBaseResponse
import com.example.musicevents.data.remote.JambaseSource
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import com.example.musicevents.ui.composable.EventItem
import com.example.musicevents.utils.Coordinates
import com.example.musicevents.utils.LocationService
import com.example.musicevents.utils.PermissionStatus
import com.example.musicevents.utils.rememberPermission
import kotlinx.coroutines.delay
import okhttp3.internal.wait

@Composable
fun HomeScreen(
    actions: HomeActions,
    state: HomeState
){
    var eventList by remember { mutableStateOf<List<EventApi>>(emptyList()) }
    var searchInput by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var events by remember { mutableStateOf<JamBaseResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    //Internet
    val ctx = LocalContext.current
    fun isOnline(): Boolean {
        val connectivityManager = ctx
            .applicationContext
            .getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }
    fun openWirelessSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(ctx.applicationContext.packageManager) != null) {
            ctx.applicationContext.startActivity(intent)
        }
    }

    //Api
    val osmDataSource = koinInject<JambaseSource>()

    val coroutineScope = rememberCoroutineScope()
    fun searchEventsFromName() = coroutineScope.launch {
        isLoading = true
        eventList = emptyList()
        if (isOnline()) {
            try {
                val res = osmDataSource.searchEvents(searchInput)
                events = res
                if (res.events.isNotEmpty()) {
                    eventList = res.events
                } else {
                    // Handle empty results case (optional: show message or placeholder)
                }
            } catch (e: Exception) {
                // Handle errors gracefully (optional: show error message or retry option)
            } finally {
                isLoading = false // Set loading state to false after finishing API call
            }
        } else {
            val res = snackbarHostState.showSnackbar(
                message = "No Internet connectivity",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                openWirelessSettings()
            }
        }
    }
    fun searchFromCoordinates(coordinates: Coordinates) = coroutineScope.launch {
        isLoading = true
        eventList = emptyList()
        if (isOnline()) {
            try {
                val res = osmDataSource.searchFromCoordinates(coordinates)
                events = res
                if (res.events.isNotEmpty()) {
                    eventList = res.events
                } else {
                    // Handle empty results case (optional: show message or placeholder)
                }
            } catch (e: Exception) {
                // Handle errors gracefully (optional: show error message or retry option)
            } finally {
                isLoading = false // Set loading state to false after finishing API call
            }
        } else {
            val res = snackbarHostState.showSnackbar(
                message = "No Internet connectivity",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                openWirelessSettings()
            }
        }
    }

    //Location

    val locationService = koinInject<LocationService>()

    val locationPermission = rememberPermission(
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) { status ->
        when (status) {
            PermissionStatus.Granted ->
                locationService.requestCurrentLocation()

            PermissionStatus.Denied ->
                actions.setShowLocationPermissionDeniedAlert(true)

            PermissionStatus.PermanentlyDenied ->
                actions.setShowLocationPermissionPermanentlyDeniedSnackbar(true)

            PermissionStatus.Unknown -> {}
        }
    }

    fun requestLocation(){
        if (locationPermission.status.isGranted) {
            locationService.requestCurrentLocation()
        } else {
            locationPermission.launchPermissionRequest()
        }
    }
    LaunchedEffect(locationService.isLocationEnabled) {
        actions.setShowLocationDisabledAlert(locationService.isLocationEnabled == false)
    }

    Column {

        val searchBtn = @Composable {
            IconButton(
                onClick = ::searchEventsFromName,
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    tint = Color.Black
                )
            }

        }
        Row(
            modifier = Modifier.padding(10.dp)
        ) {
            TextField(value = searchInput,
                onValueChange = { searchInput = it },
                modifier = Modifier
                    .padding(end = 5.dp)
                    .fillMaxWidth()
                    .weight(1f),
                trailingIcon = searchBtn
            )
            IconButton(onClick = {
                coroutineScope.launch {
                    requestLocation()
                    delay(1000)
                    val coordinate = locationService.coordinates  // Get coordinates directly
                    if (coordinate != null) {
                        searchFromCoordinates(coordinate)
                    }
                }
            }) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }

        if (state.showLocationDisabledAlert) {
            AlertDialog(
                title = { Text("Location disabled") },
                text = { Text("Location must be enabled to get your current location in the app.") },
                confirmButton = {
                    TextButton(onClick = {
                        locationService.openLocationSettings()
                        actions.setShowLocationDisabledAlert(false)
                    }) {
                        Text("Enable")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { actions.setShowLocationDisabledAlert(false) }) {
                        Text("Dismiss")
                    }
                },
                onDismissRequest = { actions.setShowLocationDisabledAlert(false) }
            )
        }

        if (state.showLocationPermissionDeniedAlert) {
            AlertDialog(
                title = { Text("Location permission denied") },
                text = { Text("Location permission is required to get your current location in the app.") },
                confirmButton = {
                    TextButton(onClick = {
                        locationPermission.launchPermissionRequest()
                        actions.setShowLocationPermissionDeniedAlert(false)
                    }) {
                        Text("Grant")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { actions.setShowLocationPermissionDeniedAlert(false) }) {
                        Text("Dismiss")
                    }
                },
                onDismissRequest = { actions.setShowLocationPermissionDeniedAlert(false) }
            )
        }

        if (state.showLocationPermissionPermanentlyDeniedSnackbar) {
            LaunchedEffect(snackbarHostState) {
                val res = snackbarHostState.showSnackbar(
                    "Location permission is required.",
                    "Go to Settings",
                    duration = SnackbarDuration.Long
                )
                if (res == SnackbarResult.ActionPerformed) {
                    ctx.startActivity(
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", ctx.packageName, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        }
                    )
                }
                actions.setShowLocationPermissionPermanentlyDeniedSnackbar(false)
            }
        }

        if(eventList.isNotEmpty()){
            LazyColumn(
                modifier = Modifier.padding(10.dp),
            ) {
                items(eventList) { item ->
                    EventItem(item = item, actions)
                }
            }
        } else if (isLoading) {
            // Show loading indicator while initially fetching events (optional)
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally))
        }
        else {
            NoEventsFound()
        }
    }

}

@Composable
fun NoEventsFound(){
    Text(
        text = "No events found",
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    )
}