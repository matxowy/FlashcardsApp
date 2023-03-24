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

    private var flashcardTextFront = DEFAULT_EMPTY_STRING
    private var flashcardTextBack = DEFAULT_EMPTY_STRING
    private var isCategorySelected = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = AddFlashcardsFragmentBinding.bind(view)

        setButtonState()
        addListeners()
        handleAddFlashcardsViewState()
        handleAddFlashcardsEvents()
    }

    private fun handleAddFlashcardsEvents() {
        viewModel.addFlashcardsEvent.observeWithLifecycle(viewLifecycleOwner) { event ->
            when (event) {
                is AddFlashcardsViewModel.AddFlashcardsEvent.ShowAddFlashcardConfirmation -> {
                    Snackbar.make(requireView(), R.string.add_flashcard_confirmation_message, Snackbar.LENGTH_LONG).show()
                }
                is AddFlashcardsViewModel.AddFlashcardsEvent.ShowDefaultError -> {
                    Snackbar.make(requireView(), R.string.default_error_message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun addListeners() {
        binding.apply {
            flashcardFront.addTextChangedListener { text ->
                flashcardTextFront = text.toString()
                setButtonState()
            }

            flashcardBack.addTextChangedListener { text ->
                flashcardTextBack = text.toString()
                setButtonState()
            }

            btnAddFlashcards.setOnClickListener {
                viewModel.onAddFlashcardClick(flashcardTextFront, flashcardTextBack)
                setDefaultView()
            }
        }
    }

    private fun setDefaultView() {
        flashcardTextBack = DEFAULT_EMPTY_STRING
        flashcardTextFront = DEFAULT_EMPTY_STRING
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
        setButtonState()
        hideKeyboard()
    }

    private fun handleAddFlashcardsViewState() {
        viewModel.viewState.observeWithLifecycle(viewLifecycleOwner) { viewState ->
            setSpinner(requireContext(), viewState.categories)
            binding.apply {
                cpiLoading.isVisible = viewState.isLoading
                tilSpinnerCategories.isVisible = viewState.isLoading.not()
            }
        }
    }

    private fun setButtonState() {
        binding.btnAddFlashcards.isEnabled = flashcardTextFront.isNotBlank() && flashcardTextBack.isNotBlank() && isCategorySelected
    }

    private fun setSpinner(context: Context, listOfCategories: List<Category>) {
        val spinnerAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, listOfCategories)
        binding.apply {
            spinnerCategories.setAdapter(spinnerAdapter)
            spinnerCategories.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                isCategorySelected = true
                setButtonState()
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
