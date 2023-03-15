package com.matxowy.flashcardsapp.data.db.di

import com.matxowy.flashcardsapp.data.db.repository.DatabaseRemoteRepository
import com.matxowy.flashcardsapp.data.db.repository.DatabaseRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindDatabaseRepository(databaseRemoteRepository: DatabaseRemoteRepository): DatabaseRepository
}
