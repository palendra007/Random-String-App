package com.pskcode.randomstringapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pskcode.randomstringapp.ui.screen.RandomStringScreen
import com.pskcode.randomstringapp.ui.viewmodel.RandomStringViewModel
import com.pskcode.randomstringapp.ui.viewmodel.RandomStringViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val factory = RandomStringViewModelFactory(applicationContext)
            val viewModel: RandomStringViewModel = viewModel(factory = factory)
            RandomStringScreen(viewModel = viewModel)
        }
    }
}