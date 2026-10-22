package org.chenx6.easydict.dao

import androidx.room.*
import org.chenx6.easydict.entities.*

@Dao
interface WordDao {
    @Query("SELECT * FROM dict WHERE word LIKE :word || '%' LIMIT :limit OFFSET :offset")
    suspend fun searchWord(word: String, limit: Int = 10, offset: Int = 0): List<WordEntity>

    @Query("SELECT dict.* FROM dict JOIN lemma ON dict.id = lemma.dict_id WHERE lemma.word = :word COLLATE NOCASE LIMIT :limit OFFSET :offset")
    suspend fun searchWordLemma(word: String, limit: Int = 10, offset: Int = 0): List<WordEntity>

    /**
     * Search words normally first, and fall back to the lemma table (inflected forms)
     * only when the normal search returns no result.
     */
    suspend fun searchWordWithLemma(
        word: String,
        limit: Int = 10,
        offset: Int = 0
    ): List<WordEntity> =
        searchWord(word, limit, offset).ifEmpty {
            searchWordLemma(word, limit, offset)
        }

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
