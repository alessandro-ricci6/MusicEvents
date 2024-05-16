package com.example.musicevents.ui.screens.settings

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.example.musicevents.ui.MusicEventsRoute
import com.example.musicevents.ui.UserActions

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    action: SettingsAction,
    themeState: ThemeState,
    userActions: UserActions
) {
    var username by remember { mutableStateOf("") }
    Scaffold(){ contentPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
            ThemeDropDown(action, themeState)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeDropDown(
    action: SettingsAction,
    themeState: ThemeState
) {
    val themeValues = Theme.entries.toList()
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(themeValues[0].name) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.height(60.dp).align(Alignment.Center),
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                themeValues.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.name) },
                        onClick = {
                            selectedText = item.name
                            action.changeTheme(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
