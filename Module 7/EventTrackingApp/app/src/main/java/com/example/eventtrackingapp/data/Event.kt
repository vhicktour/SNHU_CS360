package com.example.eventtrackingapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val note: String = "",
    val date: String = "",
    val time: String = "",
    val isDone: Boolean = false,
    val isCancelled: Boolean = false
)
