package com.matxowy.flashcardsapp.app

import android.app.Application
import androidx.room.Room
import com.matxowy.flashcardsapp.data.db.FlashcardsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideFlashcardDatabase(app: Application) =
        Room.databaseBuilder(app, FlashcardsDatabase::class.java, "flashcard_database")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    @Named("IO")
    fun provideIOCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.IO
}
