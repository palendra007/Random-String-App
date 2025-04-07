package com.pskcode.randomstringapp.data.storage

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.pskcode.randomstringapp.data.model.RandomStringData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "random_strings")

object RandomStringStore {
    private val KEY_STRINGS = stringPreferencesKey("random_strings")

    fun getRandomStrings(context: Context): Flow<List<RandomStringData>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[KEY_STRINGS] ?: "[]"
            Json.decodeFromString(json)
        }
    }

    suspend fun saveRandomStrings(context: Context, list: List<RandomStringData>) {
        val json = Json.encodeToString(list)
        context.dataStore.edit { preferences ->
            preferences[KEY_STRINGS] = json
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.remove(KEY_STRINGS) }
    }
}