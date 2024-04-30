package com.example.musicevents.ui.screens.home

import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.provider.Settings
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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

@Composable
fun HomeScreen(
    actions: HomeActions
){
    var eventList by remember { mutableStateOf<List<EventApi>>(emptyList()) }
    var searchInput by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var events by remember { mutableStateOf<JamBaseResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }

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

    val osmDataSource = koinInject<JambaseSource>()

    val coroutineScope = rememberCoroutineScope()
    fun searchEventsFromName() = coroutineScope.launch {
        isLoading = true
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "",
                    tint = Color.Black
                )
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