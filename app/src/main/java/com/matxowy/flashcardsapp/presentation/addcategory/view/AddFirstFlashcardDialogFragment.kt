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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        activity?.let {
            _binding = AddFirstFlashcardDialogBinding.inflate(layoutInflater)
            dialogView = binding.root

            AlertDialog.Builder(it).setView(dialogView)
                .setPositiveButton(R.string.add_btn_text) { _, _ ->
                    viewModel.onAddButtonClick(
                        categoryName,
                        binding.flashcardFront.text.toString(),
                        binding.flashcardBack.text.toString()
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

        addTextListenersToFields()
        handleAddFirstFlashcardEvents()
        handleAddFirstFlashcardViewState()
    }

    private fun handleAddFirstFlashcardViewState() {
        val alertDialog = dialog as AlertDialog
        viewModel.viewState.observeWithLifecycle(viewLifecycleOwner) { viewState ->
            binding.apply {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = viewState.isAddButtonEnable
            }
        }
    }

    private fun addTextListenersToFields() {
        binding.apply {
            flashcardFront.addTextChangedListener { text ->
                viewModel.setButtonState(flashcardFrontText = text.toString(), flashcardBackText = flashcardBack.text.toString())
            }

            flashcardBack.addTextChangedListener { text ->
                viewModel.setButtonState(flashcardFrontText = flashcardFront.text.toString(), flashcardBackText = text.toString())
            }
        }
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
    }
}
