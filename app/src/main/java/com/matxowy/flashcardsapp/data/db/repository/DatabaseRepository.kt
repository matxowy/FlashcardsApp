package com.matxowy.flashcardsapp.data.db.repository

import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.data.db.entity.CategoryDetail
import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    suspend fun getCategories(): Flow<List<Category>>
    suspend fun getCategoriesWithDetails(): Flow<List<CategoryDetail>>
    suspend fun getCategoryName(categoryId: Int): String
    suspend fun getCategoryNames(): List<String>
    suspend fun getFlashcardsForCategory(categoryId: Int): List<Flashcard>
    suspend fun insertCategory(categoryName: String): Long
    suspend fun insertCategory(category: Category): Long
    suspend fun insertFlashcard(flashcardFront: String, flashcardBack: String, categoryId: Int)
    suspend fun insertFlashcard(flashcard: Flashcard)
}
