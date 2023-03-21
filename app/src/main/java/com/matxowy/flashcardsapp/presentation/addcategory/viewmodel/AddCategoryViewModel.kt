package com.matxowy.flashcardsapp.presentation.addcategory.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.matxowy.flashcardsapp.app.utils.constants.ResultCodes.RESULT_ERROR
import com.matxowy.flashcardsapp.app.utils.constants.ResultCodes.RESULT_OK
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCategoryViewModel @Inject constructor() : ViewModel() {
    private val addCategoryChannel = Channel<AddCategoryEvent>(capacity = Channel.BUFFERED)
    val addCategoryEvent = addCategoryChannel.receiveAsFlow()

    fun onAddCategoryClick(categoryName: String) = AddCategoryEvent.ShowDialogForAddFlashcard(categoryName).send()

    fun onAddCategoryResult(result: Int) {
        when (result) {
            RESULT_OK -> AddCategoryEvent.ShowAddCategoryConfirmationMessage.send()
            RESULT_ERROR -> AddCategoryEvent.ShowDefaultError.send()
        }
    }

    sealed class AddCategoryEvent {
        object ShowDefaultError : AddCategoryEvent()
        object ShowAddCategoryConfirmationMessage : AddCategoryEvent()
        data class ShowDialogForAddFlashcard(val categoryName: String) : AddCategoryEvent()
    }

    private fun AddCategoryEvent.send() {
        viewModelScope.launch { addCategoryChannel.send(this@send) }
    }
}
