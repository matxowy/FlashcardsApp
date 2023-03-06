package com.matxowy.flashcardsapp.presentation.mainscreen.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.databinding.MainScreenFragmentBinding
import com.matxowy.flashcardsapp.presentation.mainscreen.viewmodel.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

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
    }

    private fun setListeners() {
        binding.btnAddCategory.setOnClickListener {
            viewModel.onAddCategoryButtonClick()
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
                }
            }
        }
    }
}
