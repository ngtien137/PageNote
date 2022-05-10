package com.lhd.apps.pagenote.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.base.baselibrary.utils.getApplication
import com.lhd.apps.pagenote.db.dao.NoteDao
import com.lhd.apps.pagenote.models.Note

@Database(entities = [Note::class], version = 1)
@TypeConverters(ListStringConverter::class)
abstract class AppDB : RoomDatabase() {

    abstract fun getNoteDao(): NoteDao

    companion object {
        private const val DB_NAME = "AppDB"
        private val dbInstance by lazy {
            Room.databaseBuilder(getApplication(), AppDB::class.java, DB_NAME)
                .allowMainThreadQueries()
                .addMigrations()
                .fallbackToDestructiveMigration()
                .build()
        }

        fun getInstance() = dbInstance
    }
}