package com.example.noteapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class RoomDatabaseNote : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object{

            @Volatile
            private var INSTANCE: RoomDatabase? = null

            fun getDatabase(context: Context): RoomDatabaseNote {
                return (INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context,
                        RoomDatabase::class.java,
                        "note_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                    instance
                }) as RoomDatabaseNote
            }
    }
}