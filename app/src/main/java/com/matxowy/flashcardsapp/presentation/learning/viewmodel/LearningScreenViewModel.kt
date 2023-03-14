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

    private lateinit var flashcardsList: List<Flashcard>

    fun loadData(categoryId: Int) = viewModelScope.launch(coroutineDispatcher) {
        flashcardsList = getFlashcardsUseCase(categoryId)
        val categoryName = getCategoryNameUseCase(categoryId)
        val firstFlashcard = flashcardsList[FIRST_FLASHCARD_INDEX]
        _viewState.update { it.copy(isLoading = false, categoryName = categoryName, flashcard = firstFlashcard) }
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val categoryName: String = EMPTY_STRING,
        val flashcard: Flashcard = Flashcard(frontText = EMPTY_STRING, backText = EMPTY_STRING, categoryId = DEFAULT_CATEGORY_ID)
    )

    companion object {
        const val FIRST_FLASHCARD_INDEX = 0
        const val DEFAULT_CATEGORY_ID = 0
        const val EMPTY_STRING = ""
    }
}
