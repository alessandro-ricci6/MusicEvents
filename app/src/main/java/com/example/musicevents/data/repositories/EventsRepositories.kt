package com.example.musicevents.data.repositories

import android.content.ContentResolver
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.database.EventsDAO
import com.example.musicevents.data.database.UserSaveEvent
import kotlinx.coroutines.flow.Flow

class EventsRepositories(
    private val eventsDAO: EventsDAO,
    private val contentResolver: ContentResolver,
    private val dataStore: DataStore<Preferences>
) {
    val events: Flow<List<Event>> = eventsDAO.getAll()

    suspend fun upsert(event: Event) {
        eventsDAO.upsert(event)
    }

    suspend fun userSaveEvent(userId: Int, event: Event){
        eventsDAO.userSaveEvent(UserSaveEvent(userId = userId, eventId = event.id))
    }
}