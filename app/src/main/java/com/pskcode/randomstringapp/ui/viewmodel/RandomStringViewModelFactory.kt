package com.pskcode.randomstringapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.pskcode.randomstringapp.repository.RandomStringRepository

class RandomStringViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RandomStringViewModel::class.java)) {
            val repository = RandomStringRepository(context)
            return RandomStringViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
