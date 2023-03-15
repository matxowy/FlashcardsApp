package com.matxowy.flashcardsapp.data.db.repository

import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getCategories(): Flow<List<Category>>
    suspend fun getCategoryName(categoryId: Int): String
    suspend fun getFlashcardsForCategory(categoryId: Int): List<Flashcard>
}
