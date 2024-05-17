package com.example.musicevents.ui.screens.home

import android.Manifest
import android.content.Intent
import android.provider.Settings
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicevents.R
import com.example.musicevents.data.remote.EventApi
import com.example.musicevents.data.remote.JamBaseResponse
import com.example.musicevents.data.remote.JambaseSource
import com.example.musicevents.data.remote.Pagination
import com.example.musicevents.ui.EventActions
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import com.example.musicevents.ui.composable.EventItem
import com.example.musicevents.utils.LocationService
import com.example.musicevents.utils.PermissionStatus
import com.example.musicevents.utils.rememberPermission
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    actions: HomeActions,
    state: HomeState,
    userId : Int,
    eventActions: EventActions
){
    var searchInput by remember { mutableStateOf("") }
    var response by remember { mutableStateOf<JamBaseResponse>(JamBaseResponse(events = emptyList(), pagination = Pagination(1, null, null))) }
    var activeGenre by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading by remember { mutableStateOf(false) }
    val ctx = LocalContext.current

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

    //Api
    LaunchedEffect(Unit) {
        if (!actions.isOnline() || state.genreList.isEmpty()) {
            val res = snackbarHostState.showSnackbar(
                message = "No Internet connectivity",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                actions.openWirelessSettings()
            }
            actions.setShowNoInternetConnectivitySnackbar(true)
            return@LaunchedEffect
        }
    }
    if(!actions.isOnline() || state.genreList.isEmpty()){
        LaunchedEffect(snackbarHostState) {
                val res = snackbarHostState.showSnackbar(
                    message = "No Internet connectivity",
                    actionLabel = "Go to Settings",
                    duration = SnackbarDuration.Long
                )
                if (res == SnackbarResult.ActionPerformed) {
                    actions.openWirelessSettings()
                }
                actions.setShowNoInternetConnectivitySnackbar(true)
                return@LaunchedEffect

        }
    }

    val jambaseDataSource = koinInject<JambaseSource>()
    val coroutineScope = rememberCoroutineScope()
    fun searchEvents(query: String) = coroutineScope.launch {
        isLoading = true
        if (actions.isOnline()) {
            try {
                response = jambaseDataSource.searchEvents(query, activeGenre)
            } catch (e: Exception) {
                // TODO
            } finally {
                Log.d("PAGE", "${response.pagination.page} - ${response.pagination.nextPage} - ${response.pagination.previousPage}")
                isLoading = false
            }
        } else {
            val res = snackbarHostState.showSnackbar(
                message = "No Internet connectivity",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                actions.openWirelessSettings()
            }
        }
    }

    fun searchPage(link: String) = coroutineScope.launch {
        isLoading = true
        if(actions.isOnline()){
            try {
                response = jambaseDataSource.searchPage(link)
            } catch (e: Exception) {
                //TODO
            } finally {
                isLoading = false
            }
        }
    }

    fun searchFromCoordinates() = coroutineScope.launch {
        isLoading = true
        requestLocation()
        delay(1000)
        val coordinate = locationService.coordinates
        if(locationService.isLocationEnabled == true && coordinate != null){
            if (actions.isOnline()) {
                try {
                    response = jambaseDataSource.searchFromCoordinates(coordinate, activeGenre, searchInput)
                } catch (e: Exception) {
                    // TODO
                } finally {
                    isLoading = false
                }
            } else {
                val res = snackbarHostState.showSnackbar(
                    message = "No Internet connectivity",
                    actionLabel = "Go to Settings",
                    duration = SnackbarDuration.Long
                )
                if (res == SnackbarResult.ActionPerformed) {
                    actions.openWirelessSettings()
                }
            }
        } else { isLoading = false }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    )
    { contentPadding ->
        Column(modifier = Modifier.padding(contentPadding)) {
            val searchBtn = @Composable {
                IconButton(
                    onClick = {
                        searchEvents(searchInput)},
                ) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search events",
                        tint = MaterialTheme.colorScheme.inverseSurface
                    )
                }
            }

            val locaBtn = @Composable {
                IconButton(onClick = {
                    coroutineScope.launch {
                        searchFromCoordinates()
                    }
                }) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.inverseSurface
                    )
                }
            }

            OutlinedTextField(
                value = searchInput,
                onValueChange = { searchInput = it },
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
                placeholder = { Text(text = "Enter artist name") },
                trailingIcon = searchBtn,
                leadingIcon = locaBtn,
                shape = RoundedCornerShape(20.dp)
            )

            Row(modifier = Modifier.padding(horizontal = 5.dp)) {
                Text(text = "Search by genre:", modifier = Modifier.align(Alignment.CenterVertically))
                LazyRow {
                    items(state.genreList) {item ->
                        Button(onClick = {
                            activeGenre = if(activeGenre != item.identifier){
                                item.identifier
                            } else { "" }},
                            modifier = Modifier.padding(5.dp),
                            border = ButtonDefaults.outlinedButtonBorder,
                            colors = if(activeGenre == item.identifier) ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary, Color.White)
                            else ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary, Color.Black),
                            shape = RoundedCornerShape(20.dp)) {
                            Text(text = item.name)
                        }
                    }
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

            if(response.events.isNotEmpty()){
                LazyColumn(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 0.dp)
                        .weight(1f),
                ) {
                    items(response.events) { item ->
                        EventItem(item = item, eventActions, userId)
                    }
                }
                Row(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    IconButton(onClick = {
                        searchPage(response.pagination.previousPage!!)},
                        enabled = response.pagination.previousPage != null,
                        modifier = Modifier.align(Alignment.CenterVertically)) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.arrow_back),
                            modifier = Modifier.size(80.dp),
                            contentDescription = "Previous Page")
                    }
                    Text(text = "${response.pagination.page}",
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 10.dp),
                        fontSize = 20.sp)
                    IconButton(onClick = {
                        searchPage(response.pagination.nextPage!!)},
                        enabled = response.pagination.nextPage != null,
                        modifier = Modifier.align(Alignment.CenterVertically)) {
                        Icon(
                            ImageVector.vectorResource(id = R.drawable.arrow_ahead),
                            modifier = Modifier.size(80.dp),
                            contentDescription = "Next Page")
                    }
                }
            } else if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 30.dp))
            }
            else {
                NoEventsFound()
            }

        }

    }

}

@Composable
fun NoEventsFound(){
    Text(
        text = "No events found",
        modifier = Modifier
            .padding(15.dp, top = 5.dp)
            .fillMaxWidth()
    )
}