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
import com.matxowy.flashcardsapp.app.utils.extensions.observeWithLifecycle
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
                btnNext.isEnabled = viewState.isLastFlashcard.not()
                btnPrevious.isVisible = viewState.isFirstFlashcard.not()
                clLearningFields.isVisible = viewState.isLoading.not()
                cpiLoading.isVisible = viewState.isLoading
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
            btnNext.setOnClickListener {
                if (isFront.not()) setFlashcardToFrontWithoutAnimation()
                viewModel.onClickButtonNext()
            }
            btnPrevious.setOnClickListener {
                if (isFront.not()) setFlashcardToFrontWithoutAnimation()
                viewModel.onClickButtonPrevious()
            }
        }
    }

    private fun setFlashcardToFrontWithoutAnimation() {
        binding.apply {
            mcvFlashcardBack.alpha = FULL_TRANSPARENT
            mcvFlashcardFront.alpha = FULL_OPAQUE
            mcvFlashcardFront.rotationY = NOT_ROTATED
            isFront = true
        }
    }

    private fun loadAnimators() {
        frontAnimation = AnimatorInflater.loadAnimator(context, R.animator.flip_front) as AnimatorSet
        backAnimation = AnimatorInflater.loadAnimator(context, R.animator.flip_back) as AnimatorSet
    }

    private fun onFlashcardClick() {
        with(binding) {
            try {
                val scale = requireContext().applicationContext.resources.displayMetrics.density
                mcvFlashcardFront.cameraDistance = DISTANCE * scale
                mcvFlashcardBack.cameraDistance = DISTANCE * scale

                if (isAnimationRunning.not()) {
                    isFront = if (isFront) {
                        frontAnimation.setTarget(mcvFlashcardFront)
                        backAnimation.setTarget(mcvFlashcardBack)
                        frontAnimation.apply {
                            doOnStart {
                                isAnimationRunning = true
                                btnNext.isClickable = false
                                btnPrevious.isClickable = false
                            }
                            start()
                        }
                        backAnimation.apply {
                            doOnEnd {
                                isAnimationRunning = false
                                btnNext.isClickable = true
                                btnPrevious.isClickable = true
                            }
                            start()
                        }
                        false
                    } else {
                        frontAnimation.setTarget(mcvFlashcardBack)
                        backAnimation.setTarget(mcvFlashcardFront)
                        backAnimation.apply {
                            doOnStart {
                                isAnimationRunning = true
                                btnNext.isClickable = false
                                btnPrevious.isClickable = false
                            }
                            start()
                        }
                        frontAnimation.apply {
                            doOnEnd {
                                isAnimationRunning = false
                                btnNext.isClickable = true
                                btnPrevious.isClickable = true
                            }
                            start()
                        }
                        true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val DISTANCE = 8000
        const val FULL_TRANSPARENT = 0.0F
        const val FULL_OPAQUE = 1.0F
        const val NOT_ROTATED = 0.0F
    }
}
