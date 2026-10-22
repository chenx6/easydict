package org.chenx6.easydict.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "lemma",
    primaryKeys = ["word", "dict_id"],
    indices = [Index(value = ["word"])]
)
data class LemmaEntity(
    val word: String,
    @ColumnInfo(name = "dict_id") val dictId: Int
)
