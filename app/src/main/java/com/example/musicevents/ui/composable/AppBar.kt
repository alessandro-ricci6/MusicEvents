package com.example.musicevents.ui.composable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.example.musicevents.R
import com.example.musicevents.ui.MusicEventsRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    navController: NavHostController,
    currentRoute: MusicEventsRoute
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                currentRoute.title,
                fontWeight = FontWeight.Medium,
            )
        },
        navigationIcon = {
            if (navController.previousBackStackEntry != null
                && navController.previousBackStackEntry!!.destination.route != MusicEventsRoute.Login.route
                && currentRoute.route != MusicEventsRoute.Login.route) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                        contentDescription = "Back button"
                    )
                }
            }
        },
        actions = {
            if(currentRoute.route != MusicEventsRoute.Profile.route
                && currentRoute.route != MusicEventsRoute.Login.route){
                IconButton(onClick = { navController.navigate(MusicEventsRoute.Profile.route) }) {
                    Icon(Icons.Default.Person, "Profile")
                }
            }
            if (currentRoute.route != MusicEventsRoute.Settings.route
                && currentRoute.route != MusicEventsRoute.Login.route) {
                IconButton(onClick = { navController.navigate(MusicEventsRoute.Settings.route) }) {
                    Icon(Icons.Outlined.Settings, "Settings")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
