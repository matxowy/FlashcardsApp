package com.matxowy.flashcardsapp.data.main.di

import com.matxowy.flashcardsapp.data.main.repository.MainScreenRemoteRepository
import com.matxowy.flashcardsapp.data.main.repository.MainScreenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MainScreenModule {
    @Binds
    @Singleton
    abstract fun bindsMainScreenRepository(mainScreenRemoteRepository: MainScreenRemoteRepository): MainScreenRepository
}
