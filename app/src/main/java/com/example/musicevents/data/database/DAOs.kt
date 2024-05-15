package com.example.musicevents.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDAO {

    @Upsert
    suspend fun userSaveEvent(userSaveEvent: UserSaveEvent)

    @Query("SELECT id FROM UserSaveEvent WHERE userId = :userId AND eventId = :eventId")
    fun getSavedEventId(userId: Int, eventId: String): List<Int>

    @Query("DELETE FROM UserSaveEvent WHERE userId = :userId AND eventId = :eventId")
    suspend fun deleteSavedEvent(userId: Int, eventId: String)

    @Query("SELECT * FROM UserSaveEvent WHERE userId = :userId AND eventId = :eventId")
    fun isEventSaved(userId: Int, eventId: String): List<UserSaveEvent>
}

@Dao
interface UserDAO {
    @Query("SELECT * FROM User")
    fun getAllUser(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE id = :id")
    fun getUserById(id: Int): User

    @Query("SELECT User.id FROM User WHERE email = :email")
    fun getIdByEmail(email: String): Int

    @Query("SELECT * FROM UserSaveEvent WHERE userId = :userId")
    fun getSavedEventsOfUser(userId: Int): Flow<List<UserSaveEvent>>

    @Query("UPDATE User SET name = :username WHERE id = :userId")
    fun changeUsername(username: String, userId: Int)

    @Upsert
    suspend fun upsert(user: User)
}