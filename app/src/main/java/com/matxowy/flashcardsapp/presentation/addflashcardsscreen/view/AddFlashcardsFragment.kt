package com.matxowy.flashcardsapp.presentation.addflashcardsscreen.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.app.utils.extensions.observeWithLifecycle
import com.matxowy.flashcardsapp.app.utils.hideKeyboard
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.databinding.AddFlashcardsFragmentBinding
import com.matxowy.flashcardsapp.presentation.addflashcardsscreen.viewmodel.AddFlashcardsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFlashcardsFragment : Fragment(R.layout.add_flashcards_fragment) {

    private val viewModel: AddFlashcardsViewModel by viewModels()
    private var _binding: AddFlashcardsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = AddFlashcardsFragmentBinding.bind(view)

        addListeners()
        handleAddFlashcardsViewState()
        handleAddFlashcardsEvents()
    }

    private fun handleAddFlashcardsEvents() {
        viewModel.addFlashcardsEvent.observeWithLifecycle(viewLifecycleOwner) { event ->
            when (event) {
                is AddFlashcardsViewModel.AddFlashcardsEvent.ShowAddFlashcardConfirmation -> {
                    setDefaultView()
                    Snackbar.make(requireView(), R.string.add_flashcard_confirmation_message, Snackbar.LENGTH_LONG).show()
                }
                is AddFlashcardsViewModel.AddFlashcardsEvent.ShowDefaultError -> {
                    setDefaultView()
                    Snackbar.make(requireView(), R.string.default_error_message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun addListeners() {
        binding.apply {
            flashcardFront.addTextChangedListener { text ->
                viewModel.setButtonState(flashcardTextFront = text.toString(), flashcardTextBack = flashcardBack.text.toString())
            }

            flashcardBack.addTextChangedListener { text ->
                viewModel.setButtonState(flashcardTextFront = flashcardFront.text.toString(), flashcardTextBack = text.toString())
            }

            btnAddFlashcards.setOnClickListener {
                viewModel.onAddFlashcardClick(flashcardTextFront = flashcardFront.text.toString(), flashcardTextBack = flashcardBack.text.toString())
            }
        }
    }

    private fun setDefaultView() {
        binding.apply {
            flashcardFront.apply {
                setText(DEFAULT_EMPTY_STRING)
                clearFocus()
            }
            flashcardBack.apply {
                setText(DEFAULT_EMPTY_STRING)
                clearFocus()
            }
        }
        hideKeyboard()
    }

    private fun handleAddFlashcardsViewState() {
        viewModel.viewState.observeWithLifecycle(viewLifecycleOwner) { viewState ->
            setSpinner(requireContext(), viewState.categories)
            binding.apply {
                cpiLoading.isVisible = viewState.isLoading
                tilSpinnerCategories.isVisible = viewState.isLoading.not()
                btnAddFlashcards.isEnabled = viewState.isButtonEnable
            }
        }
    }

    private fun setSpinner(context: Context, listOfCategories: List<Category>) {
        val spinnerAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, listOfCategories)
        binding.apply {
            spinnerCategories.setAdapter(spinnerAdapter)
            spinnerCategories.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                viewModel.setIsCategorySelected(
                    flashcardTextFront = flashcardFront.text.toString(),
                    flashcardTextBack = flashcardBack.text.toString()
                )
                viewModel.onItemSpinnerClick(listOfCategories[position].id)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DEFAULT_EMPTY_STRING = ""
    }
}
