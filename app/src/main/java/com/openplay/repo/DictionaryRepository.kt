package com.openplay.repo

import com.google.gson.Gson
import com.openplay.models.WordDefinition
import com.openplay.retrofit.RetrofitInstance
import com.openplay.room.WordDao
import com.openplay.room.WordEntity

class DictionaryRepository(private val wordDao: WordDao) {

    // Function to get word definition either from local database or remote API
    suspend fun getWordDefinition(word: String): WordDefinition? {
        // First, check the local database for the word
        val localWord = wordDao.getWord(word)
        if (localWord != null) {
            // If the word is found locally, return it
            return Gson().fromJson(localWord.definition, WordDefinition::class.java)
        } else {
            // If not found locally, fetch it from the API
            val response = RetrofitInstance.api.getWordDefinition(word)
            if (response.isSuccessful) {
                response.body()?.firstOrNull()?.let { wordDef ->
                    // Store the fetched word definition in the local database for future use
                    wordDao.insertWord(
                        WordEntity(
                            word = wordDef.word,
                            definition = Gson().toJson(wordDef)
                        )
                    )
                    return wordDef
                }
            }
        }
        // If not found or error occurs, return null
        return null
    }

}