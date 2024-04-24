package com.example.musicevents.ui.screens.home

import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.provider.Settings
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicevents.data.remote.Event
import com.example.musicevents.data.remote.JamBaseResponse
import com.example.musicevents.data.remote.JambaseSource
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun HomeScreen(){
    var eventList by remember { mutableStateOf<List<Event>>(emptyList()) }
    var searchInput by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    var events by remember { mutableStateOf<JamBaseResponse?>(null) }
    //val placeNotFound by remember { mutableStateOf(false) }

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
    fun searchEvents() = coroutineScope.launch {
        if (isOnline()) {
            val res = osmDataSource.searchEvents(searchInput)
            events = res
            if(events!!.events.isNotEmpty()){
                eventList = events!!.events
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
                onClick = ::searchEvents,
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
        TextField(value = searchInput,
            onValueChange = { searchInput = it },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            trailingIcon = searchBtn
        )

        if(eventList.isNotEmpty()){
            LazyColumn(
                modifier = Modifier.padding(10.dp),
            ) {
                items(eventList) { item ->
                    EventItem(item = item)
                }
            }
        } else {
            NoEventsFound()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(item: Event) {
    var eventSaved by remember { mutableStateOf(false) }
    val icon = if(eventSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, Color.Black),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = item.name,
                modifier = Modifier
                    .padding(10.dp)
                    .weight(1f),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                eventSaved = !eventSaved
            },
                modifier = Modifier.padding(5.dp)) {
                Icon(
                    icon,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
        Text(text = item.location.name, modifier = Modifier.padding(10.dp))
        //item.performer.iterator().forEach { Text(text = it.name) }

        AsyncImage(
            model = item.imageUrl,
            contentDescription = "The delasign logo",
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(BorderStroke(3.dp, Color.Black))
        )
        Text(
            text = "In ${item.location.city.name}, ${item.location.city.county.name} on ${item.date}",
            modifier = Modifier
                .padding(20.dp),
            fontSize = 15.sp
        )
    }
}

@Composable
fun NoEventsFound(){
    Text(text = "No events found")
}