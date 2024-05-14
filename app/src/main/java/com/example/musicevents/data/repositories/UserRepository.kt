package com.example.musicevents.data.repositories

import android.content.ContentResolver
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.musicevents.data.database.User
import com.example.musicevents.data.database.UserDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.database.UserSaveEvent
import io.ktor.util.valuesOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserRepository(
    private val userDAO: UserDAO,
    private val contentResolver: ContentResolver,
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val USER_ID = intPreferencesKey("id")
    }

    val id = dataStore.data.map { it[USER_ID] ?: 0}

    suspend fun setUser(id: Int) = dataStore.edit { it[USER_ID] = id }
    suspend fun logOut() = dataStore.edit { it[USER_ID] = 0 }

    val users: Flow<List<User>> = userDAO.getAllUser()
    val events: Flow<List<UserSaveEvent>> = userDAO.getSavedEventsOfUser(4)

    suspend fun getUserFromId(id: Int): User{
        return withContext(Dispatchers.IO) {
            val res = userDAO.getUserById(id)
            res
        }
    }

    suspend fun getEventsOfUser(userId: Int): Flow<List<UserSaveEvent>>{
        return withContext(Dispatchers.IO) {
            val res = userDAO.getSavedEventsOfUser(userId)
            res
        }
    }

    suspend fun getIdFromEmail(email: String): Int {
        return userDAO.getIdByEmail(email)
    }

    suspend fun upsert(user: User){
        userDAO.upsert(user)
    }

}