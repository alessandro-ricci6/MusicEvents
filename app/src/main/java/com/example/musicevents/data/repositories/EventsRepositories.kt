package com.example.musicevents.data.repositories

import android.content.ContentResolver
import com.example.musicevents.data.database.Event
import com.example.musicevents.data.database.EventsDAO
import kotlinx.coroutines.flow.Flow

class EventsRepositories(
    private val eventsDAO: EventsDAO,
    private val contentResolver: ContentResolver
) {
    val events: Flow<List<Event>> = eventsDAO.getAll()

    suspend fun upsert(event: Event) {}
}