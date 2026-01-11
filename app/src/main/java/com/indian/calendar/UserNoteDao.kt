package com.indian.calendar

import androidx.room.*

@Dao
interface UserNoteDao {
    @Query("SELECT * FROM user_notes")
    fun getAllNotes(): List<UserNote>

    @Query("SELECT * FROM user_notes WHERE date = :date LIMIT 1")
    fun getNoteByDate(date: String): UserNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNote(note: UserNote)

    @Update
    fun updateNote(note: UserNote)

    @Delete
    fun deleteNote(note: UserNote)
}
