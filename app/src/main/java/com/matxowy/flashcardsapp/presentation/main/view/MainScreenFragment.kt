package com.matxowy.flashcardsapp.presentation.main.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.databinding.MainScreenFragmentBinding
import com.matxowy.flashcardsapp.presentation.main.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainScreenFragment : Fragment(R.layout.main_screen_fragment) {

    private val viewModel: MainScreenViewModel by viewModels()
    private var _binding: MainScreenFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = MainScreenFragmentBinding.bind(view)

        handleMainScreenEvents()
        setListeners()
        setObservers()
    }

    private fun setObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categories.collect { listOfCategories ->
                    val namesOfTheCategories = viewModel.getNamesOfTheCategories(listOfCategories)
                    setSpinner(requireContext(), namesOfTheCategories)
                }
            }
        }
    }

    private fun setSpinner(context: Context, namesOfTheCategories: List<String>) {
        val spinnerAdapter = ArrayAdapter(context, R.layout.support_simple_spinner_dropdown_item, namesOfTheCategories)
        binding.spinnerCategories.setAdapter(spinnerAdapter)
    }

    private fun setListeners() {
        binding.apply {
            btnAddCategory.setOnClickListener {
                viewModel.onAddCategoryButtonClick()
            }
            btnAddFlashcards.setOnClickListener {
                viewModel.onAddFlashcardsButtonClick()
            }
            // TODO: Change to proper listener
            spinnerCategories.setOnClickListener {
                viewModel.onItemSpinnerClick()
            }
        }
    }

    private fun handleMainScreenEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.mainScreenEvent.collect { event ->
                when (event) {
                    MainScreenViewModel.MainScreenEvent.NavigateToAddCategory -> {
                        val action = MainScreenFragmentDirections.actionMainScreenFragmentToAddCategoryFragment()
                        findNavController().navigate(action)
                    }
                    MainScreenViewModel.MainScreenEvent.NavigateToAddFlashcards -> {
                        val action = MainScreenFragmentDirections.actionMainScreenFragmentToAddFlashcardsFragment()
                        findNavController().navigate(action)
                    }
                    MainScreenViewModel.MainScreenEvent.NavigateToLearning -> {
                        val action = MainScreenFragmentDirections.actionMainScreenFragmentToLearningScreenFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
