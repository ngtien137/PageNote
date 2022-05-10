package com.lhd.apps.pagenote.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.base.baselibrary.utils.toStringDateFormat
import java.util.*

@Entity(tableName = "tbl_note")
class Note(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L,
    @ColumnInfo(name = "contents")
    var contents: List<String> = listOf(),
    @ColumnInfo(name = "date")
    var date: Long = Date().time
) {

    @ColumnInfo(name = "font_path")
    var fontPath: String = ""

    @ColumnInfo(name = "font_size_multiple")
    var fontSizeMultiple: Float = 1f

    @Ignore
    override fun toString(): String {
        return "Note(id=$id, content='$contents', date=$date)"
    }

    @Ignore
    fun getCreatedDateString() = date.toStringDateFormat("dd/MM/yyyy")

    @Ignore
    fun getDataContentString() = contents.joinToString(separator = "\n")
}