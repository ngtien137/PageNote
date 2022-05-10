package com.lhd.apps.pagenote.repository

import com.lhd.apps.pagenote.db.AppDB
import com.lhd.apps.pagenote.models.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepository @Inject constructor() {

    private val noteDao = AppDB.getInstance().getNoteDao()

    fun insert(note: Note) = noteDao.insert(note)

    fun delete(note: Note) = noteDao.delete(note)

    fun update(note: Note) = noteDao.update(note)

    fun getAllNotes() = noteDao.getAllNotes()
}