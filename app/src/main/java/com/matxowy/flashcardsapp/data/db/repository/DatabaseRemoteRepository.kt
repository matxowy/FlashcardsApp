package com.matxowy.flashcardsapp.data.db.repository

import com.matxowy.flashcardsapp.data.db.dao.CategoryDao
import com.matxowy.flashcardsapp.data.db.dao.FlashcardDao
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import javax.inject.Inject

class DatabaseRemoteRepository @Inject constructor(
    private val categoryDao: CategoryDao,
    private val flashcardDao: FlashcardDao
) : DatabaseRepository {
    override suspend fun getCategories() = categoryDao.getCategories()

    override suspend fun getCategoryName(categoryId: Int) = categoryDao.getCategoryName(categoryId)

    override suspend fun getCategoryNames() = categoryDao.getCategoryNames()

    override suspend fun getFlashcardsForCategory(categoryId: Int) = flashcardDao.getFlashcardsForCategory(categoryId)

    override suspend fun incrementFlashcardCount(categoryId: Int) = categoryDao.incrementFlashcardCount(categoryId)

    override suspend fun insertCategory(categoryName: String) = categoryDao.insert(Category(name = categoryName))

    override suspend fun insertFlashcard(flashcardFront: String, flashcardBack: String, categoryId: Int) =
        flashcardDao.insert(Flashcard(frontText = flashcardFront, backText = flashcardBack, categoryId = categoryId))
}
