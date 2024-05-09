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
import androidx.compose.material3.OutlinedCard
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.remote.EventApi
import com.example.musicevents.ui.composable.EventItem

@Composable
fun ProfileScreen(
    userId: Int,
    actions: ProfileActions
){
    val user = actions.getUserById(userId)
    val userEvents = actions.getEventsOfUser(userId)
    Log.d("EVENTS", userEvents.size.toString())
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(model = R.drawable.def_profile, contentDescription = "Profile Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
                .clip(CircleShape)
                .size(120.dp))
        Text(text = user.name, modifier = Modifier.align(Alignment.CenterHorizontally))

        LazyColumn {
            items(userEvents) {item ->
                Text(text = item.name)
            }
        }
    }
}