package com.example.musicevents.ui.composable

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.musicevents.data.remote.EventApi
import com.example.musicevents.data.remote.Performer
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.musicevents.ui.EventActions
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

@SuppressLint("SimpleDateFormat")
@Composable
fun EventItem(item: EventApi, actions: EventActions, userId: Int) {
    var eventSaved by remember { mutableStateOf(actions.isEventSaved(userId, item.id)) }
    var showSheet by remember { mutableStateOf(false) }
    val icon = if(eventSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    val location = "${item.location.name}, ${item.location.city.name}, ${item.location.city.county.name}"
    val performer:ArrayList<String> = emptyArray<String>().toCollection(ArrayList())
    item.performer.forEach { p ->
        performer.add(p.name)
    }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd")
    dateFormat.timeZone = TimeZone.getDefault()
    val parsedDate = try {
        dateFormat.parse(item.date)
    } catch (e: ParseException) {
        Log.e("Calendar Error", "Failed to parse date", e)
        return
    }
    val date = parsedDate.time + 86400000

    val ctx = LocalContext.current
    fun addInCalendar(){
        Log.d("DATE", date.toString())
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(Uri.parse("content://com.android.calendar/events"))
            .putExtra(CalendarContract.Events.TITLE, item.name)
            .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
            .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date)
        ctx.startActivity(intent)
    }

    fun formatDate(dateString: String): String {
        if(dateString.length > 11){
            val newDate = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
            return newDate.format(DateTimeFormatter.ofPattern("EEEE yyyy-MM-dd 'at' HH:mm"))
        }
        return dateString
    }

    fun openOnMap(){
        val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:${item.location.geo.latitude},${item.location.geo.longitude}?q=${item.location.name}"))
        ctx.startActivity(mapIntent)
    }

    if (showSheet) {
        BottomSheet (onDismiss = { showSheet = false }, performerList = item.performer)
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
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
                    tint = MaterialTheme.colorScheme.onSurface
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
                .border(BorderStroke(3.dp, MaterialTheme.colorScheme.onSurface))
        )
        Text(
            text = "In ${item.location.city.name}, ${item.location.city.county.name} on ${formatDate(item.date)}",
            modifier = Modifier
                .padding(10.dp),
            fontSize = 15.sp
        )

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)) {
            Button(onClick = { showSheet = true },
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)) {
                Text(text = "See performer")
            }
            Button(onClick = { addInCalendar() },
                modifier = Modifier
                    .padding(5.dp)
                    .weight(1f)) {
                Text(text = "Add to calendar")
            }
        }
        Button(onClick = { openOnMap() },
            modifier = Modifier
                .padding(5.dp)
                .align(Alignment.CenterHorizontally)) {
            Text(text = "See on map")
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