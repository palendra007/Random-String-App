package com.pskcode.randomstringapp.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pskcode.randomstringapp.data.model.RandomStringData
import com.pskcode.randomstringapp.repository.RandomStringRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RandomStringViewModel(private val repository: RandomStringRepository) : ViewModel() {

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val stringList: StateFlow<List<RandomStringData>> = repository.getStoredStrings()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearError() {
        _error.value = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun generateRandomString(length: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getRandomString(length)
                if (result != null) {
                    repository.saveStrings(listOf(result) + stringList.value)
                } else {
                    _error.value = "No data returned"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteItem(item: RandomStringData) {
        viewModelScope.launch {
            repository.saveStrings(stringList.value.filterNot { it == item })
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearStrings()
        }
    }

    fun showError(message: String) {
        _error.value = message
    }
}