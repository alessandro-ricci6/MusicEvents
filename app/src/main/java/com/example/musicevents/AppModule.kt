package com.example.musicevents

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.musicevents.data.database.MusicEventsDatabase
import com.example.musicevents.data.remote.JambaseSource
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.data.repositories.ThemeRepository
import com.example.musicevents.data.repositories.UserRepository
import com.example.musicevents.ui.EventViewModel
import com.example.musicevents.ui.UserViewModel
import com.example.musicevents.ui.screens.eventDetail.EventDetailViewModel
import com.example.musicevents.ui.screens.home.HomeViewModel
import com.example.musicevents.ui.screens.login.LoginViewModel
import com.example.musicevents.ui.screens.profile.ProfileViewModel
import com.example.musicevents.ui.screens.settings.SettingsViewModel
import com.example.musicevents.ui.screens.venueDetail.VenueDetailViewModel
import com.example.musicevents.utils.InternetService
import com.example.musicevents.utils.LocationService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("user_pref")

val appModule = module {

    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            MusicEventsDatabase::class.java,
            "music-events"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { JambaseSource(get()) }

    single {
        UserRepository(
            get<MusicEventsDatabase>().userDAO(),
            get()
        )
    }

    single {
        EventsRepositories(
            get<MusicEventsDatabase>().eventsDAO()
        )
    }

    single {ThemeRepository(get())}

    single { LocationService(get()) }
    single { InternetService(get()) }

    viewModel { EventViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get()) }
    viewModel {SettingsViewModel(get(), get())}
    viewModel {HomeViewModel(get(), get())}
    viewModel {ProfileViewModel(get(), get())}
    viewModel {UserViewModel(get())}
    viewModel {VenueDetailViewModel()}
    viewModel {EventDetailViewModel()}
}