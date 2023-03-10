package com.matxowy.flashcardsapp.domain.main.usecase

import com.matxowy.flashcardsapp.data.main.repository.MainScreenRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: MainScreenRepository) {
    operator fun invoke() = repository.getCategories()
}
