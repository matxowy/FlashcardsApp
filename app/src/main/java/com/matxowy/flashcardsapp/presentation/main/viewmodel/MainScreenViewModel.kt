package com.matxowy.flashcardsapp.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.domain.main.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val mainScreenChannel = Channel<MainScreenEvent>(capacity = Channel.BUFFERED)
    val mainScreenEvent = mainScreenChannel.receiveAsFlow()

    val categories = getCategoriesUseCase()

    fun getNamesOfTheCategories(listOfCategories: List<Category>) = listOfCategories.map { category -> category.name }

    fun onAddCategoryButtonClick() = MainScreenEvent.NavigateToAddCategory.send()

    fun onAddFlashcardsButtonClick() = MainScreenEvent.NavigateToAddFlashcards.send()

    fun onItemSpinnerClick() = MainScreenEvent.NavigateToLearning.send()

    sealed class MainScreenEvent {
        object NavigateToAddCategory : MainScreenEvent()
        object NavigateToAddFlashcards : MainScreenEvent()
        object NavigateToLearning : MainScreenEvent()
    }

    private fun MainScreenEvent.send() {
        viewModelScope.launch { mainScreenChannel.send(this@send) }
    }
}
