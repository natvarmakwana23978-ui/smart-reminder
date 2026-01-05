package com.indian.calendar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserNote::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userNoteDao(): UserNoteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // જો ડેટાબેઝ પહેલેથી બનેલો હોય તો તે આપો, નહીં તો નવો બનાવો
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "calendar_database"
                )
                .fallbackToDestructiveMigration() // જો વર્ઝન બદલાય તો ડેટા સાફ કરી નવો સ્ટ્રક્ચર બનાવે
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

