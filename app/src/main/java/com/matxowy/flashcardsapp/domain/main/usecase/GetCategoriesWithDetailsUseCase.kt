package com.matxowy.flashcardsapp.domain.main.usecase

import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import javax.inject.Inject

class GetCategoriesWithDetailsUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    suspend operator fun invoke() = databaseRepository.getCategoriesWithDetails()
}
