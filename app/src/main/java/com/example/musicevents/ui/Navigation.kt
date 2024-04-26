package com.example.musicevents.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicevents.ui.screens.home.HomeScreen
import com.example.musicevents.ui.screens.login.LoginScreen
import com.example.musicevents.ui.screens.login.LoginViewModel
import org.koin.androidx.compose.koinViewModel

sealed class MusicEventsRoute(
    val route: String,
    val title: String,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Login : MusicEventsRoute("login", "Login")
    data object Home : MusicEventsRoute("events", "MusicEvents")
    //data object EventDetail :
    data object Settings : MusicEventsRoute("settings", "Settings")
    //data object Profile :

    companion object {
        val routes = setOf(Login, Home, Settings)
    }
}

@Composable
fun MusicEventsNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    //val placesVm = koinViewModel<PlacesViewModel>()
    //val placesState by placesVm.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = MusicEventsRoute.Login.route,
        modifier = modifier
    ){
        with(MusicEventsRoute.Home) {
            composable(route) {
                HomeScreen()
            }
        }
        with(MusicEventsRoute.Login) {
            composable(route) {
                val loginVm = koinViewModel<LoginViewModel>()
                val userState by loginVm.state.collectAsStateWithLifecycle()
                LoginScreen(navController, userState, loginVm.actions)
            }
        }
    }
}