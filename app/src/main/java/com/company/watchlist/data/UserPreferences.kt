package com.company.watchlist.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "USER_PREFERENCES")
class UserPreferences(private val context: Context) {

    private object PreferencesKeys {
        val KEY_USER_FNAME = stringPreferencesKey("KEY_USER_FNAME")
        val KEY_USER_LNAME = stringPreferencesKey("KEY_USER_LNAME")
    }

    val userFName: Flow<String>
        get() = context.dataStore.data.map { preference ->
            preference[PreferencesKeys.KEY_USER_FNAME] ?: ""
        }

    suspend fun userFName(fName: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_USER_FNAME] = fName
        }
    }

    val userLName: Flow<String>
        get() = context.dataStore.data.map { preference ->
            preference[PreferencesKeys.KEY_USER_LNAME] ?: ""
        }

    suspend fun userLName(lName: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.KEY_USER_LNAME] = lName
        }
    }

}