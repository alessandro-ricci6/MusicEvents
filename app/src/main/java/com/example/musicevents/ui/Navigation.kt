package com.example.musicevents.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController

sealed class MusicEventsRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    //data object Home :
    //data object EventDetail :
    //data object Setting :
    //data object Profile :
}

@Composable
fun MusicEventsNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    //val eventsVm = koinViewModel<>()
}