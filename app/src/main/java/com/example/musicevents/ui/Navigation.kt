package com.example.musicevents.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Place
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

    data object EventDetail: MusicEventsRoute("eventDetail", "Event Detail", Icons.Outlined.Info)

    data object VenueDetail: MusicEventsRoute("venueDetail", "Venue Detail", Icons.Outlined.Place)

    companion object {
        val routes = setOf(Login, Home, Settings, Profile, EventDetail, VenueDetail)
    }
}

@Composable
fun MusicEventsNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val loginVm = koinViewModel<LoginViewModel>()
    val homeVm = koinViewModel<HomeViewModel>()
    val userVm = koinViewModel<UserViewModel>()
    val userId by userVm.userId.collectAsStateWithLifecycle()
    val eventVm = koinViewModel<EventViewModel>()

    NavHost(
        navController = navController,
        startDestination = if (userId == 0) MusicEventsRoute.Login.route else MusicEventsRoute.Home.route,
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
                LoginScreen(navController, loginVm.actions)
            }
        }
        with(MusicEventsRoute.Settings) {
            composable(route) {
                val settingVm = koinViewModel<SettingsViewModel>()
                val themeState by settingVm.state.collectAsStateWithLifecycle()
                SettingsScreen(navController, settingVm.actions, userVm.actions, themeState)
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