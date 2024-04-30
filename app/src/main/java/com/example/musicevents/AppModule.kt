package com.example.musicevents

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import org.koin.androidx.viewmodel.dsl.viewModel
import com.example.musicevents.data.database.MusicEventsDatabase
import com.example.musicevents.data.remote.JambaseSource
import com.example.musicevents.data.repositories.EventsRepositories
import com.example.musicevents.data.repositories.UserRepository
import com.example.musicevents.ui.screens.home.HomeViewModel
import com.example.musicevents.ui.screens.login.LoginViewModel
import com.example.musicevents.ui.screens.profile.ProfileViewModel
import com.example.musicevents.ui.screens.settings.SettingsViewModel
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
            // Sconsigliato per progetti seri! Lo usiamo solo qui per semplicità
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
            get<Context>().applicationContext.contentResolver,
            get()
        )
    }

    single {
        EventsRepositories(
            get<MusicEventsDatabase>().eventsDAO(),
            get<Context>().applicationContext.contentResolver,
            get()
        )
    }

    viewModel { LoginViewModel(get()) }
    viewModel {SettingsViewModel(get())}
    viewModel {HomeViewModel(get(), get())}
    viewModel {ProfileViewModel(get(), get())}
}