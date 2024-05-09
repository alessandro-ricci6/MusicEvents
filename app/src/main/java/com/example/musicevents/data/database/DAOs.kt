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

    @Query("SELECT id FROM UserSaveEvent WHERE userId = :userId AND eventId = :eventId")
    fun getSavedEventId(userId: Int, eventId: String): List<Int>

    @Query("SELECT Event.* FROM Event INNER JOIN UserSaveEvent ON Event.id = UserSaveEvent.eventId WHERE UserSaveEvent.userId = :userId")
    fun getAllEventOfUser(userId: Int): List<Event>

    @Query("DELETE FROM UserSaveEvent WHERE id = :id")
    suspend fun deleteSavedEvent(id: Int)

    @Query("SELECT * FROM UserSaveEvent WHERE userId = :userId AND eventId = :eventId")
    fun isEventSaved(userId: Int, eventId: String): List<UserSaveEvent>

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