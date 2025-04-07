package com.pskcode.randomstringapp.repository

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.pskcode.randomstringapp.data.model.RandomStringData
import com.pskcode.randomstringapp.data.storage.RandomStringStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject

class RandomStringRepository(private val context: Context) {

    suspend fun getRandomString(length: Int): RandomStringData? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return null

        val uri = Uri.parse("content://com.iav.contestdataprovider/text")
            .buildUpon()
            .appendQueryParameter("length", length.toString())
            .build()

        val args = Bundle().apply {
            putInt(ContentResolver.QUERY_ARG_LIMIT, 1)
        }

        return withContext(Dispatchers.IO) {
            context.contentResolver.query(uri, null, args, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val jsonString = cursor.getString(cursor.getColumnIndexOrThrow("data"))
                    parseJsonToRandomStringData(jsonString)
                } else {
                    null
                }
            }
        }
    }

    private fun parseJsonToRandomStringData(jsonString: String): RandomStringData? {
        return try {
            val json = JSONObject(jsonString)
            val text = json.getJSONObject("randomText")
            RandomStringData(
                value = text.getString("value"),
                length = text.getInt("length"),
                created = text.getString("created")
            )
        } catch (e: JSONException) {
            Log.e("RandomStringRepository", "JSON parsing error", e)
            null
        }
    }

    fun getStoredStrings(): Flow<List<RandomStringData>> {
        return RandomStringStore.getRandomStrings(context)
    }

    suspend fun saveStrings(list: List<RandomStringData>) {
        RandomStringStore.saveRandomStrings(context, list)
    }

    suspend fun clearStrings() {
        RandomStringStore.clear(context)
    }
}
