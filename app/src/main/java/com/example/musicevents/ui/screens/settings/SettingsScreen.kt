package com.example.musicevents.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.musicevents.data.models.Theme
import com.example.musicevents.ui.MusicEventsRoute
import com.example.musicevents.ui.UserActions

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    action: SettingsAction,
    userActions: UserActions,
    themeState: ThemeState
) {
    var username by remember { mutableStateOf("") }
    Scaffold{ contentPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)) {
            ThemeButtons(themeState = themeState, action)
            HorizontalDivider()
            OutlinedTextField(value = username,
                onValueChange = {username = it},
                label = { Text(text = "Username") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 10.dp),
                trailingIcon = {
                    IconButton(onClick = {
                        userActions.changeUsername(username)
                    }) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Change username"
                        )
                    }
                })
            HorizontalDivider()
            ListItem(
                headlineContent = { TextButton(
                    onClick = { action.logOut()
                        navHostController.navigate(MusicEventsRoute.Login.route)},
                    modifier = Modifier.fillMaxSize()) {
                    Text(text = "Log out")
                } },
                modifier = Modifier
                    .height(75.dp)
                    .fillMaxWidth()
            )
            HorizontalDivider()

        }
    }
}

@Composable
fun ThemeButtons(themeState: ThemeState, action: SettingsAction){
    Column {
        Text(text = "Select the theme", modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp))
        Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally)) {
            Theme.entries.forEach { theme ->
                Button(onClick = { action.changeTheme(theme) },
                    modifier = Modifier.weight(1f).padding(horizontal = 5.dp)) {
                    Text(text = theme.name)
                    if(themeState.theme == theme){
                        Icon(Icons.Default.Check, contentDescription = "Selected")
                    }
                }
            }
        }
    }
}
