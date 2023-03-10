package com.matxowy.flashcardsapp.presentation.learning.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import com.matxowy.flashcardsapp.domain.learning.GetCategoryNameUseCase
import com.matxowy.flashcardsapp.domain.learning.GetFlashcardsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class LearningScreenViewModel @Inject constructor(
    private val getCategoryNameUseCase: GetCategoryNameUseCase,
    private val getFlashcardsUseCase: GetFlashcardsUseCase,
    @Named("IO") private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    fun loadData(categoryId: Int) = viewModelScope.launch(coroutineDispatcher) {
        val categoryName = getCategoryNameUseCase(categoryId)
        val flashcardsList = getFlashcardsUseCase(categoryId)
        _viewState.update { it.copy(isLoading = false, categoryName = categoryName, flashcards = flashcardsList) }
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val categoryName: String = "",
        val flashcards: List<Flashcard> = emptyList()
    )
}
