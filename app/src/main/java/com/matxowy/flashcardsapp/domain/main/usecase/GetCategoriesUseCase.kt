package com.matxowy.flashcardsapp.domain.main.usecase

import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val databaseRepository: DatabaseRepository) {
    operator fun invoke() = databaseRepository.getCategories()
}
