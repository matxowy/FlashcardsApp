package com.matxowy.flashcardsapp.domain.availablecategories.usecase

import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import javax.inject.Inject

class AddFlashcardForCategoryUseCase @Inject constructor(private val repository: DatabaseRepository) {
    suspend operator fun invoke(flashcard: Flashcard) = repository.insertFlashcard(flashcard)
}
