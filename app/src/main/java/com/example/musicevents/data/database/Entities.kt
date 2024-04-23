package com.example.musicevents.data.database

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation

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
data class Event (
    @PrimaryKey
    val id: Int,

    @ColumnInfo
    val name: String,

    @ColumnInfo
    val venue: String,

    @ColumnInfo
    val performer: String,

    @ColumnInfo
    val date: String
)

data class UserEventCrossRef(
    val userId: Long,
    val eventId: Long
)

data class UserWithEvents(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "eventId",
        associateBy = Junction(UserEventCrossRef::class)
    )
    val events: List<Event>
)

data class EventWithUsers(
    @Embedded val event: Event,
    @Relation(
        parentColumn = "eventId",
        entityColumn = "userId",
        associateBy = Junction(UserEventCrossRef::class)
    )
    val users: List<User>
)