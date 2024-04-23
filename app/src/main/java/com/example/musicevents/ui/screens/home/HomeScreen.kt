package com.example.musicevents.ui.screens.home

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.musicevents.R
import com.example.musicevents.data.database.Event

@Composable
fun HomeScreen(){
    Column {
        SearchBar()
        LazyColumn(
            modifier = Modifier.padding(10.dp)
        ) {
            items(15) { index ->
                EventItem(item = Event(id=index, name="I-Days", venue="Milan", performer="Bring me the horizon", date="07/08/2024"))
            }
        }
    }
}

@Composable
fun SearchBar(){
    var searchInput by remember { mutableStateOf("") }
    val searchBtn = @Composable {
        IconButton(
            onClick = {
                { /* TODO */ }
            },
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
        Row {
            Text(
                text = "${item.name}  ${item.performer}",
                modifier = Modifier
                    .padding(12.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = {
                eventSaved = !eventSaved
            }) {
                Icon(
                    icon,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
        AsyncImage(
            model = "https://www.jambase.com/wp-content/uploads/2017/02/bring-me-the-horizon-bring-me-the-horizon-9f7d1441-dc68-4499-8c27-e14216d3cf24_220241_RETINA_LANDSCAPE_16_9-1480x832.jpg",
            contentDescription = "The delasign logo",
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .border(BorderStroke(3.dp, Color.Black))
        )
        Text(
            text = "In ${item.venue} on ${item.date}",
            modifier = Modifier
                .padding(20.dp),
            fontSize = 15.sp
        )
    }
}