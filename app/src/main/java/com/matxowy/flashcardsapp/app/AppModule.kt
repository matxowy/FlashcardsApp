package com.matxowy.flashcardsapp.app

import android.app.Application
import androidx.room.Room
import com.matxowy.flashcardsapp.data.db.FlashcardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Named
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideFlashcardDatabase(app: Application, callback: FlashcardsDatabase.Callback) =
        Room.databaseBuilder(app, FlashcardsDatabase::class.java, "flashcard_database")
            .addCallback(callback)
            .build()

    @Provides
    @Singleton
    fun provideCategoryDao(db: FlashcardsDatabase) = db.categoryDao()

    @Provides
    @Singleton
    fun provideFlashcardDao(db: FlashcardsDatabase) = db.flashcardDao()

    @Provides
    @Singleton
    @Named("IO")
    fun provideIOCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope() = CoroutineScope(SupervisorJob())
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope
