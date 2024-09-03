package com.openplay.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.openplay.models.WordDefinition
import com.openplay.repo.DictionaryRepository
import com.openplay.room.WordDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DictionaryRepository
    // LiveData for word definition
    private val _wordDefinition = MutableLiveData<WordDefinition?>()
    val wordDefinition: LiveData<WordDefinition?> = _wordDefinition

    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        val wordDao = WordDatabase.getDatabase(application).wordDao()
        repository = DictionaryRepository(wordDao)
    }

    fun fetchWordDefinition(word: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                // Fetch word definition from the repository
                val definition = repository.getWordDefinition(word)

                withContext(Dispatchers.Main) {
                    if (definition != null) {
                        // If a definition is found (either from cache or API), update LiveData
                        _wordDefinition.value = definition
                    } else {
                        // If not found, show an appropriate message or handle accordingly
                        _wordDefinition.value = null
                        // You could also set an error message or state here
                    }
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                // Handle any errors, like network issues
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                    _wordDefinition.value = null // Clear the current value if there's an error
                    // Optional: Set an error message or log the error
                }
            }
        }
    }
}