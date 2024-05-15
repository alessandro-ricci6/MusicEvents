package com.example.musicevents.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.example.musicevents.ui.screens.profile.ProfileViewModel
import com.example.musicevents.ui.screens.settings.SettingsScreen
import com.example.musicevents.ui.screens.settings.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

sealed class MusicEventsRoute(
    val route: String,
    val title: String,
    val icon: ImageVector,
    val arguments: List<NamedNavArgument> = emptyList()
) {
    data object Login : MusicEventsRoute("login", "Login", Icons.Outlined.Lock)
    data object Home : MusicEventsRoute("events", "MusicEvents", Icons.Outlined.Home)
    //data object EventDetail :
    data object Settings : MusicEventsRoute("settings", "Settings", Icons.Outlined.Settings)
    //data object Profile :
    data object Profile: MusicEventsRoute("profile", "Profile", Icons.Outlined.Person)

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
    val homeVm = koinViewModel<HomeViewModel>()
    val userVm = koinViewModel<UserViewModel>()
    val userId = userVm.userId.collectAsStateWithLifecycle().value
    val eventVm = koinViewModel<EventViewModel>()

    NavHost(
        navController = navController,
        startDestination = if (userId != 0) MusicEventsRoute.Home.route else MusicEventsRoute.Login.route,
        modifier = modifier
    ){
        with(MusicEventsRoute.Home) {
            composable(route) {
                val state by homeVm.state.collectAsStateWithLifecycle()
                HomeScreen(homeVm.actions, state, userId, eventVm.actions)
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
                val themeState = settingVm.state.collectAsStateWithLifecycle()
                SettingsScreen(navController, settingVm.actions, themeState.value, userVm.actions)
            }
        }
        with(MusicEventsRoute.Profile){
            composable(route){
                val profileVm = koinViewModel<ProfileViewModel>()
                val profileState by profileVm.state.collectAsState()
                ProfileScreen(userId, profileVm.actions, profileState, eventVm.actions)
            }
        }
    }
}