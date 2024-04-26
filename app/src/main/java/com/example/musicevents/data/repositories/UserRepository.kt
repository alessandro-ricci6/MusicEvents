package com.example.musicevents.data.repositories

import android.content.ContentResolver
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.musicevents.data.database.User
import com.example.musicevents.data.database.UserDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences


class UserRepository(
    private val userDAO: UserDAO,
    private val contentResolver: ContentResolver,
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val USERNAME_KEY = stringPreferencesKey("email")
    }

    val email = dataStore.data.map { it[USERNAME_KEY] ?: "" }

    suspend fun setUser(value: String) = dataStore.edit { it[USERNAME_KEY] = value }

    val users: Flow<List<User>> = userDAO.getAllUser()

    suspend fun upsert(user: User){
        userDAO.upsert(user)
    }
}