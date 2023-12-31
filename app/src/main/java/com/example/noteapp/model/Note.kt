package com.example.noteapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title : String,

    @ColumnInfo(name = "content")
    val content : String,

    @ColumnInfo(name = "timestamp")
    val timestamp : String

        )