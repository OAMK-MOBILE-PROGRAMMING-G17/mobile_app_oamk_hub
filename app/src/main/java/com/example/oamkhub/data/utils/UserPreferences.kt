package com.example.oamkhub.data.utils

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_ID = stringPreferencesKey("user_id")
    }

    fun getToken(): String? = runBlocking {
        context.dataStore.data
            .map { preferences -> preferences[USER_TOKEN] }
            .first()
    }

    fun getUserId(): String? = runBlocking {
        context.dataStore.data
            .map { preferences -> preferences[USER_ID] }
            .first()
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
        Log.d("USER_DEBUG", "Saved userId to DataStore: $userId")

    }


    suspend fun clearAll() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
            preferences.remove(USER_ID)
        }
    }
}
