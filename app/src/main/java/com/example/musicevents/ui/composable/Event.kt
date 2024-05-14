package com.example.musicevents.ui.composable

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.example.musicevents.data.remote.EventApi
import com.example.musicevents.data.remote.Performer
import com.example.musicevents.ui.screens.home.HomeActions
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

@Composable
fun EventItem(item: EventApi, actions: HomeActions, userId: Int) {
    var eventSaved by remember { mutableStateOf(actions.isEventSaved(userId, item.id)) }
    var showSheet by remember { mutableStateOf(false) }
    val icon = if(eventSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    val performer:ArrayList<String> = emptyArray<String>().toCollection(ArrayList())
    item.performer.forEach { p ->
        performer.add(p.name)
    }

    if (showSheet) {
        BottomSheet (onDismiss = { showSheet = false }, performerList = item.performer)
    }

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
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                if (eventSaved) {
                    actions.deleteSavedEvents(item.id)
                } else {
                    actions.saveEvent(item.id)
                }
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

        AsyncImage(
            model = item.imageUrl,
            contentDescription = "Event Image",
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(BorderStroke(3.dp, Color.Black))
        )
        Text(
            text = "In ${item.location.city.name}, ${item.location.city.county.name} on ${item.date}",
            modifier = Modifier
                .padding(10.dp),
            fontSize = 15.sp
        )

        Button(onClick = { showSheet = true },
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 5.dp)) {
            Text(text = "See performer")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onDismiss: () -> Unit, performerList: List<Performer>) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        modifier = Modifier.defaultMinSize(minHeight = 150.dp)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 40.dp)
        ) {
            items(performerList) { item ->
                Text(text = item.name,
                    modifier = Modifier.padding(7.dp))
            }
        }
    }
}