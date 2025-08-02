package com.giftcard8.todoapp.utils

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {
    companion object {
        val KEY_USER_ID = intPreferencesKey("user_id")
        val KEY_TOKEN = stringPreferencesKey("token")
        val KEY_USERNAME = stringPreferencesKey("username")
    }

    suspend fun saveUser(
        id: Int,
        username: String,
        token: String,
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_ID] = id
            prefs[KEY_USERNAME] = username
            prefs[KEY_TOKEN] = token
        }
    }

    fun getUserId(): Flow<Int?> = context.dataStore.data.map { it[KEY_USER_ID] }

    fun getToken(): Flow<String?> = context.dataStore.data.map { it[KEY_TOKEN] }

    fun getUsername(): Flow<String?> = context.dataStore.data.map { it[KEY_USERNAME] }

    suspend fun logout() {
        context.dataStore.edit { it.clear() }
    }
}
