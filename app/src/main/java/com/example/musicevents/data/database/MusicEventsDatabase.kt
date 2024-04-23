package com.example.musicevents.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Event::class, User::class], version = 2)
abstract class MusicEventsDatabase : RoomDatabase() {
    //abstract fun eventsDAO(): DAO
}