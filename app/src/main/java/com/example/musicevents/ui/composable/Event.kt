package com.example.musicevents.ui.composable

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.remote.EventApi
import com.example.musicevents.ui.screens.home.HomeActions

@Composable
fun EventItem(item: EventApi, actions: HomeActions) {
    var eventSaved by remember { mutableStateOf(false) }
    val icon = if(eventSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    val eventId = item.id
    val eventLocation = "${item.location.name} in ${item.location.city.name}, ${item.location.city.county.name}"
    val eventPerformer: String = ""
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
                actions.saveEvent(
                    Event(name = item.name, date = item.date,
                    imageUrl = item.imageUrl, venue = eventLocation, performer = eventPerformer, id = item.id)
                )
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