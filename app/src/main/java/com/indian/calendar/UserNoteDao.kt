package com.indian.calendar

import androidx.room.*

@Dao
interface UserNoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: UserNote)

    @Query("SELECT * FROM user_notes")
    suspend fun getAllNotes(): List<UserNote>

    @Delete
    suspend fun deleteNote(note: UserNote)
}
