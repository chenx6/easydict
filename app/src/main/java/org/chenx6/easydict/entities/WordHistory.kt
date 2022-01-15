package org.chenx6.easydict.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class WordHistory(
    @PrimaryKey val wordId: Int,
    val queryTime: Int
)

data class WordAndHistory(
    @Embedded val word: WordEntity,
    @Relation(parentColumn = "id", entityColumn = "wordId")
    val wordHistory: WordHistory
)