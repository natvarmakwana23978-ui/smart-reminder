package com.indian.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_notes")
data class UserNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val note: String,
    val festivalName: String? = null
)
