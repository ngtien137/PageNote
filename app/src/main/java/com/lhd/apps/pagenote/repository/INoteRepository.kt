package com.lhd.apps.pagenote.repository

import com.lhd.apps.pagenote.models.Note

interface INoteRepository {

    fun getNoteRepository(): NoteRepository

    fun insert(note: Note) = getNoteRepository().insert(note)
    fun update(note: Note) = getNoteRepository().update(note)
    fun delete(note: Note) = getNoteRepository().delete(note)



}