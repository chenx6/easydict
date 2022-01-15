package org.chenx6.easydict

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import org.chenx6.easydict.dao.WordDao
import org.chenx6.easydict.entities.WordEntity
import org.chenx6.easydict.entities.WordFavorite
import org.chenx6.easydict.entities.WordHistory

@Database(
    version = 2,
    exportSchema = false,
    entities = [WordEntity::class, WordHistory::class, WordFavorite::class]
)
abstract class WordDatabase : RoomDatabase() {
    abstract fun getWordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: WordDatabase? = null

        fun getInstance(context: Context): WordDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE wordhistory(wordId INTEGER PRIMARY KEY NOT NULL, queryTime INTEGER NOT NULL);")
                database.execSQL("CREATE TABLE wordfavorite(wordId INTEGER PRIMARY KEY NOT NULL, favoriteTime INTEGER NOT NULL);")
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, WordDatabase::class.java, "dict.db")
                .createFromAsset("dict.db")
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}