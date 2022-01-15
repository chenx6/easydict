package org.chenx6.easydict.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity
data class WordFavorite(
    @PrimaryKey val wordId: Int,
    val favoriteTime: Int
)

data class WordAndFavorite(
    @Embedded val word: WordEntity,
    @Relation(parentColumn = "id", entityColumn = "wordId")
    val favorite: WordFavorite
)