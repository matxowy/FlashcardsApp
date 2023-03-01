package com.matxowy.flashcardsapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.matxowy.flashcardsapp.data.db.dao.CategoryDao
import com.matxowy.flashcardsapp.data.db.dao.FlashcardDao
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.data.db.entity.Flashcard

@Database(
    entities = [Category::class, Flashcard::class],
    version = 1
)
abstract class FlashcardsDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun flashcardDao(): FlashcardDao
}
