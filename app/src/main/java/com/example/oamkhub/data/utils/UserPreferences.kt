package com.example.oamkhub.data.utils

import android.content.Context
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val USER_TOKEN = stringPreferencesKey("user_token")
    }

    fun getToken(): String? = runBlocking {
        context.dataStore.data
            .map { preferences -> preferences[USER_TOKEN] }
            .first()
    }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    suspend fun clearToken() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_TOKEN)
        }
    }
}
