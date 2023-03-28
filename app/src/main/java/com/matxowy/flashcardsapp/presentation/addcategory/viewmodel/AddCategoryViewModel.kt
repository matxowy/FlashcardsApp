package com.matxowy.flashcardsapp.presentation.addcategory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.app.utils.constants.ResultCodes.RESULT_ERROR
import com.matxowy.flashcardsapp.app.utils.constants.ResultCodes.RESULT_OK
import com.matxowy.flashcardsapp.domain.addcategory.usecase.GetCategoryNamesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AddCategoryViewModel @Inject constructor(
    private val getCategoryNamesUseCase: GetCategoryNamesUseCase,
    @Named("IO") private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val addCategoryChannel = Channel<AddCategoryEvent>(capacity = Channel.BUFFERED)
    val addCategoryEvent = addCategoryChannel.receiveAsFlow()

    fun onAddCategoryClick(categoryName: String) = viewModelScope.launch(coroutineDispatcher) {
        val categoryNames = getCategoryNamesUseCase()
        if (categoryNames.contains(categoryName)) {
            AddCategoryEvent.ShowNameAlreadyExistsError.send()
        } else {
            AddCategoryEvent.ShowDialogForAddFlashcard(categoryName).send()
        }
    }

    fun onAddCategoryResult(result: Int) {
        when (result) {
            RESULT_OK -> AddCategoryEvent.ShowAddCategoryConfirmationMessage.send()
            RESULT_ERROR -> AddCategoryEvent.ShowDefaultError.send()
        }
    }

    sealed class AddCategoryEvent {
        object ShowDefaultError : AddCategoryEvent()
        object ShowNameAlreadyExistsError : AddCategoryEvent()
        object ShowAddCategoryConfirmationMessage : AddCategoryEvent()
        data class ShowDialogForAddFlashcard(val categoryName: String) : AddCategoryEvent()
    }

    private fun AddCategoryEvent.send() {
        viewModelScope.launch { addCategoryChannel.send(this@send) }
    }
}
