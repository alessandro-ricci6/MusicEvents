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

    @Upsert
    suspend fun userSaveEvent(userSaveEvent: UserSaveEvent)
    @Delete
    suspend fun delete(item: Event)
}

@Dao
interface UserDAO {
    @Query("SELECT * FROM User")
    fun getAllUser(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: Int): User

    @Query("SELECT User.id FROM User WHERE email = :email")
    fun getIdByEmail(email: String): Int

    @Upsert
    suspend fun upsert(user: User)
}