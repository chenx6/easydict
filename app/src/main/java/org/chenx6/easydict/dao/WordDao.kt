package org.chenx6.easydict.dao

import androidx.room.*
import org.chenx6.easydict.entities.*

@Dao
interface WordDao {
    @Query("SELECT * FROM dict WHERE word LIKE :word || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchWord(word: String, limit: Int = 10, offset: Int = 0): List<WordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addHistory(history: WordHistory)

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM dict, wordhistory WHERE dict.id = wordhistory.wordId LIMIT :limit OFFSET :offset")
    suspend fun getHistory(limit: Int = 10, offset: Int = 0): List<WordAndHistory>

    @Insert
    suspend fun addFavorite(favorite: WordFavorite)

    @Delete
    suspend fun deleteFavorite(favorite: WordFavorite)

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM dict, wordfavorite WHERE dict.id = wordfavorite.wordId LIMIT :limit OFFSET :offset")
    suspend fun getFavorite(limit: Int = 10, offset: Int = 0): List<WordAndFavorite>

    @Query("SELECT 1 FROM wordfavorite WHERE :wordId = wordfavorite.wordId")
    suspend fun isFavorite(wordId: Int): Int?

    @Delete
    suspend fun deleteQueryHistory(word: WordHistory)

    @Query("DELETE FROM wordhistory")
    suspend fun cleanAllQueryHistory()
}