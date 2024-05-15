package com.example.musicevents.ui.screens.profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.musicevents.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.musicevents.data.remote.EventApi
import com.example.musicevents.data.remote.JambaseSource
import com.example.musicevents.ui.EventActions
import com.example.musicevents.ui.composable.EventItem
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun ProfileScreen(
    userId: Int,
    actions: ProfileActions,
    profileState: ProfileState,
    eventActions: EventActions
){
    val user = actions.getUserById(userId)
    val snackbarHostState = remember { SnackbarHostState() }
    var eventList by remember { mutableStateOf<List<EventApi>>(emptyList()) }
    val jambaseDataSource = koinInject<JambaseSource>()
    val coroutineScope = rememberCoroutineScope()
    Log.d("STATE", profileState.events.size.toString())

    LaunchedEffect(Unit) {
        if (!actions.isOnline()) {
            actions.setShowNoInternetConnectivitySnackbar(true)
            return@LaunchedEffect
        }
    }

    fun getEvents(eventId: String) =  coroutineScope.launch {
        if (actions.isOnline()) {
            try {
                val res = jambaseDataSource.getEvent(eventId)
                eventList += res.event
            } catch (e: Exception) {
                // TODO
            } finally {
                // TODO
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

    profileState.events.forEach { event ->
        LaunchedEffect(event) {
            getEvents(event.eventId)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ){contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .fillMaxSize()
        ) {
            AsyncImage(model = R.drawable.def_profile, contentDescription = "Profile Image",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(10.dp)
                    .clip(CircleShape)
                    .size(120.dp))
            Text(text = user.name, modifier = Modifier.align(Alignment.CenterHorizontally))

            if(eventList.isNotEmpty()){
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 0.dp),
                ) {
                    items(eventList) {item ->
                        EventItem(item = item, actions = eventActions, userId = userId)
                    }
                }
            } else {
                Text(text = "No events Saved", modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}