package com.example.musicevents.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.musicevents.ui.screens.home.HomeScreen
import com.example.musicevents.ui.screens.home.HomeViewModel
import com.example.musicevents.ui.screens.login.LoginScreen
import com.example.musicevents.ui.screens.login.LoginViewModel
import com.example.musicevents.ui.screens.profile.ProfileScreen
import com.example.musicevents.ui.screens.settings.SettingsScreen
import com.example.musicevents.ui.screens.settings.SettingsViewModel
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
    data object Profile: MusicEventsRoute("profile", "Profile")

    companion object {
        val routes = setOf(Login, Home, Settings, Profile)
    }
}

@Composable
fun MusicEventsNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    //val placesVm = koinViewModel<PlacesViewModel>()
    //val placesState by placesVm.state.collectAsStateWithLifecycle()
    val loginVm = koinViewModel<LoginViewModel>()
    val userState by loginVm.state.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = if (loginVm.userLogged.id != 0) MusicEventsRoute.Home.route else MusicEventsRoute.Login.route,
        modifier = modifier
    ){
        with(MusicEventsRoute.Home) {
            composable(route) {
                val homeVm = koinViewModel<HomeViewModel>()
                HomeScreen(homeVm.actions)
            }
        }
        with(MusicEventsRoute.Login) {
            composable(route) {
                LoginScreen(navController, userState, loginVm.actions)
            }
        }
        with(MusicEventsRoute.Settings) {
            composable(route) {
                val settingVm = koinViewModel<SettingsViewModel>()
                SettingsScreen(navController, settingVm.actions)
            }
        }
        with(MusicEventsRoute.Profile){
            composable(route){
                ProfileScreen()
            }
        }
    }
}