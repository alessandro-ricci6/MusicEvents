package com.example.musicevents.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun ProfileScreen(
    //user: User
){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(model = R.drawable.def_profile, contentDescription = "Profile Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(10.dp)
                .clip(CircleShape)
                .size(120.dp))
        Text(text = "User Name", modifier = Modifier.align(Alignment.CenterHorizontally))

        LazyColumn {

        }
    }
}