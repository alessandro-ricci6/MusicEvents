package com.example.musicevents.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class, UserSaveEvent::class], version = 5)
@TypeConverters(ArrayConverter::class)
abstract class MusicEventsDatabase : RoomDatabase() {
    abstract fun eventsDAO(): EventsDAO
    abstract fun userDAO(): UserDAO
}