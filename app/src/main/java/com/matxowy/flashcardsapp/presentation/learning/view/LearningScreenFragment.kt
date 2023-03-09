package com.matxowy.flashcardsapp.presentation.learning.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.matxowy.flashcardsapp.R
import com.matxowy.flashcardsapp.databinding.LearningScreenFragmentBinding
import com.matxowy.flashcardsapp.presentation.learning.viewmodel.LearningScreenViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LearningScreenFragment : Fragment(R.layout.learning_screen_fragment) {

    private val viewModel: LearningScreenViewModel by viewModels()
    private var _binding: LearningScreenFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var frontAnimation: AnimatorSet
    private lateinit var backAnimation: AnimatorSet
    private var isFront = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = LearningScreenFragmentBinding.bind(view)

        loadAnimators()
        setListeners()
    }

    private fun setListeners() {
        binding.apply {
            clFlashcardFront.setOnClickListener {
                onFlashcardClick()
            }

            clFlashcardBack.setOnClickListener {
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
            binding.clFlashcardFront.cameraDistance = DISTANCE * scale
            binding.clFlashcardBack.cameraDistance = DISTANCE * scale

            isFront = if (isFront) {
                frontAnimation.setTarget(binding.clFlashcardBack)
                backAnimation.setTarget(binding.clFlashcardFront)
                frontAnimation.start()
                backAnimation.start()
                false
            } else {
                frontAnimation.setTarget(binding.clFlashcardFront)
                backAnimation.setTarget(binding.clFlashcardBack)
                backAnimation.start()
                frontAnimation.start()
                true
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
