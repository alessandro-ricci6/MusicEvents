package com.example.musicevents.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDAO {
    @Query("SELECT * FROM Event")
    fun getAll(): Flow<List<Event>>

    @Upsert
    suspend fun upsert(event: Event)

    @Delete
    suspend fun delete(item: Event)
}

@Dao
interface UserDAO {
    @Query("SELECT * FROM User")
    fun getAllUser(): Flow<List<User>>

    @Upsert
    fun upsert(user: User)
}