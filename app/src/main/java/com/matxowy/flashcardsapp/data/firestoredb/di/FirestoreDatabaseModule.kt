package com.matxowy.flashcardsapp.data.firestoredb.di

import com.matxowy.flashcardsapp.data.firestoredb.repository.FirestoreRemoteRepository
import com.matxowy.flashcardsapp.data.firestoredb.repository.FirestoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirestoreDatabaseModule {

    @Binds
    @Singleton
    abstract fun bindFirestoreDatabaseRepository(firestoreRemoteRepository: FirestoreRemoteRepository): FirestoreRepository
}
