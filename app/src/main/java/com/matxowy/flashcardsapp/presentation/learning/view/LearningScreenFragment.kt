package com.matxowy.flashcardsapp.presentation.learning.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.app.utils.observeWithLifecycle
import com.matxowy.flashcardsapp.databinding.LearningScreenFragmentBinding
import com.matxowy.flashcardsapp.presentation.learning.viewmodel.LearningScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearningScreenFragment : Fragment(R.layout.learning_screen_fragment) {

    private val viewModel: LearningScreenViewModel by viewModels()
    private val args: LearningScreenFragmentArgs by navArgs()
    private var _binding: LearningScreenFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var frontAnimation: AnimatorSet
    private lateinit var backAnimation: AnimatorSet
    private var isFront = true
    private var isAnimationRunning = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = LearningScreenFragmentBinding.bind(view)

        loadAnimators()
        setListeners()
        loadData()
        handleViewState()
    }

    private fun loadData() {
        viewModel.loadData(args.categoryId)
    }

    private fun handleViewState() {
        viewModel.viewState.observeWithLifecycle(viewLifecycleOwner) { viewState ->
            binding.apply {
                cpiLoading.isVisible = viewState.isLoading
                clLearningFields.isVisible = viewState.isLoading.not()
                mtvCategoryName.text = viewState.categoryName
                mtvFlashcardFront.text = viewState.flashcard.frontText
                mtvFlashcardBack.text = viewState.flashcard.backText
            }
        }
    }

    private fun setListeners() {
        binding.apply {
            mcvFlashcardFront.setOnClickListener {
                onFlashcardClick()
            }

            mcvFlashcardBack.setOnClickListener {
                onFlashcardClick()
            }
        }
    }

    private fun loadAnimators() {
        frontAnimation = AnimatorInflater.loadAnimator(context, R.animator.flip_front) as AnimatorSet
        backAnimation = AnimatorInflater.loadAnimator(context, R.animator.flip_back) as AnimatorSet
    }

    private fun onFlashcardClick() {
        try {
            val scale = requireContext().applicationContext.resources.displayMetrics.density
            binding.mcvFlashcardFront.cameraDistance = DISTANCE * scale
            binding.mcvFlashcardBack.cameraDistance = DISTANCE * scale

            if (isAnimationRunning.not()) {
                isFront = if (isFront) {
                    frontAnimation.setTarget(binding.mcvFlashcardBack)
                    backAnimation.setTarget(binding.mcvFlashcardFront)
                    frontAnimation.apply {
                        doOnStart { isAnimationRunning = true }
                        start()
                    }
                    backAnimation.apply {
                        doOnEnd { isAnimationRunning = false }
                        start()
                    }
                    false
                } else {
                    frontAnimation.setTarget(binding.mcvFlashcardFront)
                    backAnimation.setTarget(binding.mcvFlashcardBack)
                    backAnimation.apply {
                        doOnStart { isAnimationRunning = true }
                        start()
                    }
                    frontAnimation.apply {
                        doOnEnd { isAnimationRunning = false }
                        start()
                    }
                    true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val DISTANCE = 8000
    }
}
