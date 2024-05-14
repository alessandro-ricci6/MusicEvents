package com.example.musicevents.data.repositories

import android.content.ContentResolver
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.musicevents.data.database.EventsDAO
import com.example.musicevents.data.database.User
import com.example.musicevents.data.database.UserSaveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class EventsRepositories(
    private val eventsDAO: EventsDAO,
    private val contentResolver: ContentResolver,
    private val dataStore: DataStore<Preferences>
) {

    suspend fun userSaveEvent(userId: Int, eventId: String){
        eventsDAO.userSaveEvent(UserSaveEvent(userId = userId, eventId = eventId))
    }

     suspend fun isEventSaved(userId: Int, eventId: String): Boolean{
        return withContext(Dispatchers.IO) {
            val res = eventsDAO.isEventSaved(userId, eventId)
            Log.d("SAVEDD", res.toString())
            res.isNotEmpty()
        }
    }

    suspend fun deleteSavedEvent(userId: Int, eventId: String){
        eventsDAO.deleteSavedEvent(userId, eventId)
    }

    suspend fun getSavedEventId(userId: Int, eventId: String): Int {
        return withContext(Dispatchers.IO) {
            val id = eventsDAO.getSavedEventId(userId, eventId)
            id.first()
        }
    }
}