package com.matxowy.flashcardsapp.data.db

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.app.ApplicationScope
import com.matxowy.flashcardsapp.data.db.dao.CategoryDao
import com.matxowy.flashcardsapp.data.db.dao.FlashcardDao
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.data.db.entity.CategoryDetail
import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(
    entities = [Category::class, Flashcard::class],
    views = [CategoryDetail::class],
    version = 2,
    autoMigrations = [AutoMigration(from = 1, to = 2)]
)
abstract class FlashcardsDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun flashcardDao(): FlashcardDao

    class Callback @Inject constructor(
        private val database: Provider<FlashcardsDatabase>,
        @ApplicationContext private val context: Context,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val categoryDao = database.get().categoryDao()
            val flashcardDao = database.get().flashcardDao()

            applicationScope.launch {
                categoryDao.insert(
                    Category(name = context.getString(R.string.default_category_name))
                )
                flashcardDao.insert(
                    Flashcard(
                        frontText = context.getString(R.string.default_front_text),
                        backText = context.getString(R.string.default_back_text),
                        categoryId = 1
                    )
                )
            }
        }
    }
}
