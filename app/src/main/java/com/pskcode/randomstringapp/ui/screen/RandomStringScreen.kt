package com.pskcode.randomstringapp.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import com.pskcode.randomstringapp.data.model.RandomStringData
import com.pskcode.randomstringapp.ui.viewmodel.RandomStringViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RandomStringScreen(viewModel: RandomStringViewModel) {
    var inputLength by remember { mutableStateOf("10") }
    val stringList by viewModel.stringList.collectAsState()
    val error by viewModel.error.collectAsState()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputLength,
                onValueChange = {
                    if (it.all { ch -> ch.isDigit() }) inputLength = it
                },
                label = { Text("String Length") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            Button(
                onClick = {
                    val length = inputLength.toIntOrNull()
                    if (length != null && length > 0) {
                        viewModel.generateRandomString(length)
                    } else {
                        viewModel.showError("Enter a valid length")
                    }
                }
            ) {
                Text("Generate")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (stringList.isNotEmpty()) {
            Button(
                onClick = { viewModel.clearAll() },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Clear All")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(stringList) { item ->
                RandomStringCard(item, onDelete = { viewModel.deleteItem(it) })
            }
        }

        error?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Snackbar(
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("Dismiss")
                    }
                }
            ) {
                Text(it)
            }
        }
    }
}

@Composable
fun RandomStringCard(item: RandomStringData, onDelete: (RandomStringData) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Value: ${item.value}", style = MaterialTheme.typography.bodyLarge)
            Text("Length: ${item.length}", style = MaterialTheme.typography.bodyMedium)
            Text("Created: ${item.created}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { onDelete(item) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Delete")
            }
        }
    }
}