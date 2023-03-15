package com.matxowy.flashcardsapp.data.db.repository

import com.matxowy.flashcardsapp.data.db.dao.CategoryDao
import com.matxowy.flashcardsapp.data.db.dao.FlashcardDao
import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import javax.inject.Inject

class DatabaseRemoteRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val flashcardDao: FlashcardDao
) : DatabaseRepository {
    override fun getCategories() = categoryDao.getCategories()

    override suspend fun getCategoryName(categoryId: Int) = categoryDao.getCategoryName(categoryId)

    override suspend fun getFlashcardsForCategory(categoryId: Int): List<Flashcard> = flashcardDao.getFlashcardsForCategory(categoryId)
}
