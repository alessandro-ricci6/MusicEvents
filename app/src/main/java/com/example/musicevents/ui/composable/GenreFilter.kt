package com.example.musicevents.ui.composable

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.musicevents.data.remote.Genre
import com.example.musicevents.data.remote.JambaseSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun GenreFilter(){
    val snackbarHostState = remember { SnackbarHostState() }
    var genreList by remember { mutableStateOf<List<Genre>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    //Internet
    val ctx = LocalContext.current
    fun isOnline(): Boolean {
        val connectivityManager = ctx
            .applicationContext
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true ||
                capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) == true
    }
    fun openWirelessSettings() {
        val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (intent.resolveActivity(ctx.applicationContext.packageManager) != null) {
            ctx.applicationContext.startActivity(intent)
        }
    }

    val jambaseDataSource = koinInject<JambaseSource>()
    fun getAllGenres() =  coroutineScope.launch {
        if (isOnline()) {
            try {
                val res = jambaseDataSource.getAllGenres()
                genreList = res.genres
            } catch (e: Exception) {
                // Handle errors gracefully (optional: show error message or retry option)
            } finally {
               // Set loading state to false after finishing API call
            }
        } else {
            val res = snackbarHostState.showSnackbar(
                message = "No Internet connectivity",
                actionLabel = "Go to Settings",
                duration = SnackbarDuration.Long
            )
            if (res == SnackbarResult.ActionPerformed) {
                openWirelessSettings()
            }
        }
    }

    getAllGenres()
    Text(text = genreList.size.toString())
}