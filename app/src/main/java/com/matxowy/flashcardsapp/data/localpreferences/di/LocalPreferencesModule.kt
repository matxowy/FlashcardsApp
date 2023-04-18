package com.matxowy.flashcardsapp.data.localpreferences.di

import com.matxowy.flashcardsapp.data.localpreferences.LocalPreferencesRemoteRepository
import com.matxowy.flashcardsapp.data.localpreferences.LocalPreferencesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocalPreferencesModule {

    @Binds
    @Singleton
    abstract fun provideLocalPreferences(localPreferencesRemoteRepository: LocalPreferencesRemoteRepository): LocalPreferencesRepository
}
