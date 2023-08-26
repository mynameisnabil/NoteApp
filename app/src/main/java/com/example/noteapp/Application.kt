package com.example.noteapp

import android.app.Application
import com.example.noteapp.model.RoomDatabaseNote

class Application : Application() {

    val database: RoomDatabaseNote by lazy { RoomDatabaseNote.getDatabase(this) }
}