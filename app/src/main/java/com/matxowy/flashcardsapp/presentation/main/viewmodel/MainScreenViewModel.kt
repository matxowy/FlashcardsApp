package com.matxowy.flashcardsapp.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.data.db.entity.CategoryDetail
import com.matxowy.flashcardsapp.domain.main.usecase.GetCategoriesWithDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getCategoriesWithDetailsUseCase: GetCategoriesWithDetailsUseCase,
    @Named("IO") private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val mainScreenChannel = Channel<MainScreenEvent>(capacity = Channel.BUFFERED)
    val mainScreenEvent = mainScreenChannel.receiveAsFlow()

    private val _viewState = MutableStateFlow(ViewState())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch(coroutineDispatcher) {
            val categories = getCategoriesWithDetailsUseCase()
            categories.collectLatest { categoriesList ->
                _viewState.update { it.copy(isLoading = false, categories = categoriesList) }
            }
        }
    }

    fun onAddCategoryButtonClick() = MainScreenEvent.NavigateToAddCategory.send()

    fun onAddFlashcardsButtonClick() = MainScreenEvent.NavigateToAddFlashcards.send()

    fun onCategoryItemClick(categoryId: Int) = MainScreenEvent.NavigateToLearning(categoryId).send()

    sealed class MainScreenEvent {
        object NavigateToAddCategory : MainScreenEvent()
        object NavigateToAddFlashcards : MainScreenEvent()
        data class NavigateToLearning(val categoryId: Int) : MainScreenEvent()
    }

    data class ViewState(
        val isLoading: Boolean = true,
        val categories: List<CategoryDetail> = emptyList()
    )

    private fun MainScreenEvent.send() {
        viewModelScope.launch { mainScreenChannel.send(this@send) }
    }
}
