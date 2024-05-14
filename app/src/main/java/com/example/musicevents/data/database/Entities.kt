package com.example.musicevents.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverter

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val password: String,

    @ColumnInfo
    val email: String
)

@Entity
data class UserSaveEvent (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo
    val userId: Int,

    @ColumnInfo
    val eventId: String
)
