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

    // આ ફંક્શન વિજેટ માટે જરૂરી છે
    @Query("SELECT * FROM user_notes WHERE date = :date LIMIT 1")
    fun getNoteByDate(date: String): UserNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: UserNote)

    @Update
    fun updateNote(note: UserNote)

    @Delete
    fun deleteNote(note: UserNote)
}
