package com.example.musicevents.data.repositories

import android.content.ContentResolver
import com.example.musicevents.data.database.User
import com.example.musicevents.data.database.UserDAO
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val userDAO: UserDAO,
    private val contentResolver: ContentResolver
) {
    val users: Flow<List<User>> = userDAO.getAllUser()

    suspend fun upsert(user: User){
        userDAO.upsert(user)
    }
}