package com.indian.calendar

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserNoteDao {
    @Query("SELECT * FROM user_notes")
    fun getAllNotes(): List<UserNote>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: UserNote)

    @Update
    fun updateNote(note: UserNote)

    @Delete
    fun deleteNote(note: UserNote)
}
