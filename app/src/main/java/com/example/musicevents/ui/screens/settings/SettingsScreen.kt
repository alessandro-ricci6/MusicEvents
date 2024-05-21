package com.example.musicevents.ui.screens.settings

import android.Manifest
import android.widget.Toast
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.musicevents.R
import com.example.musicevents.data.models.Theme
import com.example.musicevents.ui.MusicEventsRoute
import com.example.musicevents.ui.UserActions
import com.example.musicevents.utils.rememberCameraLauncher
import com.example.musicevents.utils.rememberPermission

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    action: SettingsAction,
    userActions: UserActions,
    themeState: ThemeState
) {
    var username by remember { mutableStateOf("") }
    val ctx = LocalContext.current

    //Camera
    val cameraLauncher = rememberCameraLauncher { imageUri ->
        action.setImage(imageUri)
    }

    val cameraPermission = rememberPermission(Manifest.permission.CAMERA) { status ->
        if (status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            Toast.makeText(ctx, "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    fun takePicture() {
        if (cameraPermission.status.isGranted) {
            cameraLauncher.captureImage()
        } else {
            cameraPermission.launchPermissionRequest()
        }
    }

    Scaffold{ contentPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)) {
            ThemeButtons(themeState = themeState, action)
            HorizontalDivider()
            Text(text = "Change username:", modifier = Modifier.align(Alignment.CenterHorizontally).padding(5.dp))
            OutlinedTextField(value = username,
                onValueChange = {username = it},
                label = { Text(text = "Username") },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 5.dp),
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
            Text(text = "Change profile image:", modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(5.dp))
            Button(onClick = ::takePicture, modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 5.dp)) {
                Text(text = "Change image", modifier = Modifier.padding(end = 5.dp))
                Icon(ImageVector.vectorResource(id = R.drawable.camera), contentDescription = "Camera")
            }
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
        Text(text = "Select the theme", modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(5.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = 5.dp)) {
            Theme.entries.forEach { theme ->
                Button(onClick = { action.changeTheme(theme) },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 5.dp)) {
                    Text(text = theme.name)
                    if(themeState.theme == theme){
                        Icon(Icons.Default.Check, contentDescription = "Selected")
                    }
                }
            }
        }
    }
}
