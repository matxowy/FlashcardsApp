package com.matxowy.flashcardsapp.domain.availablecategories.usecase

import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import javax.inject.Inject

class AddCategoryToLocalDatabaseUseCase @Inject constructor(private val repository: DatabaseRepository) {
    suspend operator fun invoke(category: Category) = repository.insertCategory(category)
}
