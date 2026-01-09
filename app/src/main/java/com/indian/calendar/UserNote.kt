package com.indian.calendar

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_notes")
data class UserNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,          // વિજેટ આ તારીખથી ડેટા શોધશે
    val personalNote: String,  // આ નામ વિજેટમાં વપરાયું છે (Unresolved reference સુધારો)
    val isReligious: Boolean = false
)
