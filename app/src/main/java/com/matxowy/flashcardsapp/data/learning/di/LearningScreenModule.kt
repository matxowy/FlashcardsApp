package com.matxowy.flashcardsapp.data.learning.di

import com.matxowy.flashcardsapp.data.learning.repository.LearningScreenRemoteRepository
import com.matxowy.flashcardsapp.data.learning.repository.LearningScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LearningScreenModule {

    @Binds
    @Singleton
    abstract fun bindLearningScreenRepository(learningScreenRemoteRepository: LearningScreenRemoteRepository): LearningScreenRepository
}
