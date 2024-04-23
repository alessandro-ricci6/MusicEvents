package com.example.musicevents.ui.screens.home

import android.graphics.drawable.Icon
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.musicevents.data.database.Event

@Composable
fun HomeScreen(){
    LazyColumn(
        modifier = Modifier.padding(15.dp)
    ) {
        items(15) { index ->
            EventItem(item = Event(id=index, name="I-Days", venue="Milan", performer="Bring me the horizon", date="07/08/2024"))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventItem(item: Event) {
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
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    Icons.Default.FavoriteBorder,
                    contentDescription = "",
                    tint = Color.Black
                )
            }
        }
        Text(
            text = "In ${item.venue} on ${item.date}",
            modifier = Modifier
                .padding(horizontal = 20.dp),
            fontSize = 15.sp
        )
    }
}