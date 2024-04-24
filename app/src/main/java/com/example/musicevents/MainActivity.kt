package com.example.musicevents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.musicevents.ui.theme.MusicEventsTheme
import androidx.compose.runtime.derivedStateOf
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicevents.ui.MusicEventsNavGraph
import com.example.musicevents.ui.MusicEventsRoute
import com.example.musicevents.ui.composable.AppBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicEventsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //HomeScreen()

                    val navController = rememberNavController()
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute by remember {
                        derivedStateOf {
                            MusicEventsRoute.routes.find {
                                it.route == backStackEntry?.destination?.route
                            } ?: MusicEventsRoute.Home
                        }
                    }

                    Scaffold(
                        topBar = { AppBar(navController, currentRoute) }
                    ) { contentPadding ->
                        MusicEventsNavGraph(
                            navController,
                            modifier =  Modifier.padding(contentPadding)
                        )
                    }
                }
            }
        }
    }
}
