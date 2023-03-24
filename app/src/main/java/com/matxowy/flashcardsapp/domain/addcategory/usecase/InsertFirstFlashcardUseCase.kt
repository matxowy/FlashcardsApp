package com.matxowy.flashcardsapp.domain.addcategory.usecase

import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import javax.inject.Inject

class InsertFirstFlashcardUseCase @Inject constructor(
    private val databaseRepository: DatabaseRepository
) {
    suspend operator fun invoke(flashcardFront: String, flashcardBack: String, categoryId: Int) =
        databaseRepository.insertFlashcard(flashcardFront, flashcardBack, categoryId)
}
