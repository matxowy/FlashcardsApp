package com.matxowy.flashcardsapp.data.firestoredb.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.matxowy.flashcardsapp.app.utils.constants.LanguageCodes.POLISH
import com.matxowy.flashcardsapp.data.db.entity.Category
import com.matxowy.flashcardsapp.data.db.entity.Flashcard
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebaseFields.BACK_TEXT_FIELD
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebaseFields.CATEGORY_ID_FIELD
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebaseFields.FRONT_TEXT_FIELD
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebaseFields.ID_FIELD
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebaseFields.NAME_FIELD
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebasePaths.AVAILABLE_CATEGORIES_PATH
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebasePaths.CATEGORIES_PATH
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebasePaths.ENGLISH_PATH
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebasePaths.FLASHCARDS_PATH
import com.matxowy.flashcardsapp.data.firestoredb.constants.FirebasePaths.POLISH_PATH
import com.matxowy.flashcardsapp.data.localpreferences.LocalPreferencesRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRemoteRepository @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val localPreferencesRepository: LocalPreferencesRepository
) : FirestoreRepository {

    override suspend fun getAvailableCategories(language: String): List<Category> {
        val collectionRef = when (language) {
            POLISH -> firebaseFirestore.collection(CATEGORIES_PATH).document(POLISH_PATH).collection(AVAILABLE_CATEGORIES_PATH)
            else -> firebaseFirestore.collection(CATEGORIES_PATH).document(ENGLISH_PATH).collection(AVAILABLE_CATEGORIES_PATH)
        }
        val downloadedCategoryIds = localPreferencesRepository.getDownloadedCategoryIds() ?: setOf()
        val listOfAvailableCategories = mutableListOf<Category>()
        try {
            val querySnapshot = collectionRef.get().await()
            for (document in querySnapshot.documents) {
                document.getString(ID_FIELD)?.let { id ->
                    document.getString(NAME_FIELD)?.let { name ->
                        if (downloadedCategoryIds.contains(id).not()) {
                            listOfAvailableCategories.add(Category(id = id.toInt(), name = name))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return listOfAvailableCategories
    }

    override suspend fun getFlashcardsForCategory(language: String, categoryName: String): List<Flashcard> {
        val collectionRef = when (language) {
            POLISH -> firebaseFirestore.collection(FLASHCARDS_PATH).document(POLISH_PATH).collection(categoryName)
            else -> firebaseFirestore.collection(FLASHCARDS_PATH).document(ENGLISH_PATH).collection(categoryName)
        }
        val listOfFlashcards = mutableListOf<Flashcard>()
        try {
            val querySnapshot = collectionRef.get().await()
            for (document in querySnapshot.documents) {
                document.getString(CATEGORY_ID_FIELD)?.let { id ->
                    document.getString(BACK_TEXT_FIELD)?.let { backText ->
                        document.getString(FRONT_TEXT_FIELD)?.let { frontText ->
                            listOfFlashcards.add(Flashcard(frontText = frontText, backText = backText, categoryId = id.toInt()))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return emptyList()
        }
        return listOfFlashcards
    }
}
