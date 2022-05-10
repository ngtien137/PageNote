package com.lhd.apps.pagenote.db.dao

import androidx.room.*
import com.lhd.apps.pagenote.db.ListStringConverter
import com.lhd.apps.pagenote.models.Note

@Dao
@TypeConverters(ListStringConverter::class)
interface NoteDao {

    @Query("select * from tbl_note")
    fun getAllNotes(): List<Note>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(note: Note)

    @Delete
    fun delete(note: Note)


}