package com.matxowy.flashcardsapp.presentation.addcategory.view

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.app.utils.extensions.observeWithLifecycle
import com.matxowy.flashcardsapp.databinding.AddFirstFlashcardDialogBinding
import com.matxowy.flashcardsapp.presentation.addcategory.viewmodel.AddFirstFlashcardDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFirstFlashcardDialogFragment(private val categoryName: String) : DialogFragment() {

    private val viewModel: AddFirstFlashcardDialogViewModel by viewModels()
    private var _binding: AddFirstFlashcardDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var dialogView: View

    private var flashcardTextFront = DEFAULT_EMPTY_STRING
    private var flashcardTextBack = DEFAULT_EMPTY_STRING

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        activity?.let {
            _binding = AddFirstFlashcardDialogBinding.inflate(layoutInflater)
            dialogView = binding.root

            AlertDialog.Builder(it).setView(dialogView)
                .setPositiveButton(R.string.add_btn_text) { _, _ ->
                    viewModel.onAddButtonClick(
                        categoryName,
                        flashcardTextFront,
                        flashcardTextBack
                    )
                }
                .setNegativeButton(R.string.cancel_btn_text) { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
        } ?: throw IllegalStateException(EXCEPTION_MESSAGE)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return dialogView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAddFirstFlashcardEvents()
    }

    override fun onResume() {
        super.onResume()
        val alertDialog = dialog as AlertDialog

        setButtonPositiveToDisabled(alertDialog)
        addTextListenersToFields(alertDialog)
    }

    private fun addTextListenersToFields(alertDialog: AlertDialog) {
        binding.apply {
            flashcardFront.addTextChangedListener { text ->
                flashcardTextFront = text.toString()
                setButtonState(alertDialog)
            }

            flashcardBack.addTextChangedListener { text ->
                flashcardTextBack = text.toString()
                setButtonState(alertDialog)
            }
        }
    }

    private fun setButtonState(alertDialog: AlertDialog) {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = flashcardTextFront.isNotBlank() && flashcardTextBack.isNotBlank()
    }

    private fun setButtonPositiveToDisabled(alertDialog: AlertDialog) {
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }

    private fun handleAddFirstFlashcardEvents() {
        viewModel.addFirstFlashcardEvent.observeWithLifecycle(viewLifecycleOwner) { event ->
            when (event) {
                is AddFirstFlashcardDialogViewModel.AddFirstFlashcardDialogEvent.SetFragmentResult -> {
                    setFragmentResult(
                        requestKey = ADD_FIRST_FLASHCARD_REQUEST,
                        result = bundleOf(ADD_FIRST_FLASHCARD_RESULT to event.result)
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val ADD_FIRST_FLASHCARD_REQUEST = "add_first_flashcard_request"
        const val ADD_FIRST_FLASHCARD_RESULT = "add_first_flashcard_result"
        const val EXCEPTION_MESSAGE = "Activity cannot be null"
        const val DEFAULT_EMPTY_STRING = ""
    }
}
