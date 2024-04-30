package com.example.musicevents.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Event::class, User::class, UserSaveEvent::class], version = 3)
abstract class MusicEventsDatabase : RoomDatabase() {
    abstract fun eventsDAO(): EventsDAO
    abstract fun userDAO(): UserDAO
}