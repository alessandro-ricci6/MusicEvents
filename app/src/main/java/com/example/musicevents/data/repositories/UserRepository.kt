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
import androidx.datastore.preferences.core.intPreferencesKey


class UserRepository(
    private val userDAO: UserDAO,
    private val contentResolver: ContentResolver,
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val USER_ID = intPreferencesKey("id")
    }

    val id = dataStore.data.map { it[USER_ID] }

    suspend fun setUser(id: Int) = dataStore.edit { it[USER_ID] = id }
    suspend fun logOut() = dataStore.edit { it[USER_ID] = 0 }

    val users: Flow<List<User>> = userDAO.getAllUser()

    suspend fun getUserFromId(id: Int): User{
        return userDAO.getUserById(id)
    }

    suspend fun upsert(user: User){
        userDAO.upsert(user)
    }
}