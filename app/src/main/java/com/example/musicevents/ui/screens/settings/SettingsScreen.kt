package com.example.musicevents.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.musicevents.ui.MusicEventsRoute

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    action: SettingsAction
) {
    Column {
        ListItem(
            headlineContent = { Button(
                onClick = { action.logOut()
                    navHostController.navigate(MusicEventsRoute.Login.route)},
                modifier = Modifier.fillMaxSize()) {
                Text(text = "Log out")
            } },
            modifier = Modifier.height(75.dp).fillMaxWidth()
        )
        HorizontalDivider()

    }
}