package com.example.noteapp.model

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: Note)

    @Update
    suspend fun update(note: Note)

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note WHERE id = :id")
    fun getSpecificNote(id: Int): Flow<Note>

    @Query("SELECT * FROM note")
    fun getAllNotes(): Flow<List<Note>>
}