package org.chenx6.easydict.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "dict", indices = arrayOf(Index(value = ["word"], unique = true), Index(value = ["sw", "word"])))
data class WordEntity(
    @PrimaryKey val id: Int,
    val word: String,
    val sw: String,
    val phonetic: String?,
    val definition: String?,
    val translation: String?,
    val pos: String?,
    @ColumnInfo(defaultValue = "0") val collins: Int?,
    @ColumnInfo(defaultValue = "0") val oxford: Int?,
    val tag: String?,
    val bnc: Int?,
    val frq: Int?,
    val exchange: String?,
    val detail: String?,
    val audio: String?
)