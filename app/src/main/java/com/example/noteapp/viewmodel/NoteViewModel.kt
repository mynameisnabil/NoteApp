package com.example.noteapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.noteapp.model.Note
import com.example.noteapp.model.NoteDao
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class NoteViewModel (private val noteDao: NoteDao) : ViewModel() {


    val allNote: LiveData<List<Note>> = noteDao.getAllNotes().asLiveData()

    private fun createNewNote(TitleNote: String, ContentNote: String, TimeNote:String): Note{
        return Note(
            title = TitleNote,
            content = ContentNote,
            timestamp = TimeNote

        )
    }

    private fun insert(note: Note) {
        viewModelScope.launch { noteDao.insert(note) }
    }

    private fun update(note: Note) {
        viewModelScope.launch { noteDao.update(note) }
    }

    private fun getUpdate(
          id: Int,
            TitleNote: String,
            ContentNote: String,
            TimeNote: String
    ): Note {
        return Note(
            id = id,
            title = TitleNote,
            content = ContentNote,
            timestamp = TimeNote
        )
    }

    fun addNewNote(
        TitleNote: String,
        ContentNote: String,
        TimeNote: String
    ) {
        val newNote = createNewNote(
            TitleNote,
            ContentNote,
            TimeNote
        )
        insert(newNote)
    }

    fun isEntryValid(TitleNote: String, ContentNote: String): Boolean {
        if (TitleNote.isBlank() || ContentNote.isBlank()) {
            return false
        }
        return true
    }

    fun getSpecificNote (id: Int): LiveData<Note> {
        return noteDao.getSpecificNote (id).asLiveData()
    }

    fun updateNote(
        id: Int,
        TitleNote: String,
        ContentNote: String,
        TimeNote: String
    ) {
        val updatedNote = getUpdate(
            id,
            TitleNote,
            ContentNote,
            TimeNote
        )
        update(updatedNote)
    }

    fun delete(note: Note) {
        viewModelScope.launch { noteDao.delete(note) }
    }


}

class NoteViewModelFactory(private val noteDao: NoteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(noteDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

