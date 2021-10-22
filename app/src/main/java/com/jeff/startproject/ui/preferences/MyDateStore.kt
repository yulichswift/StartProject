package com.jeff.startproject.ui.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jeff.startproject.MyApplication
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

object MyDateStore {
    private const val DATA_STORE_NAME = "Jeff"
    private val CONTENT_PREF_KEY = stringPreferencesKey("CONTENT")
    private fun getContext() = MyApplication.applicationContext()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)
    val profileFlow: Flow<String> = getContext().dataStore.data
        .catch {
            if (it is IOException) {
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            it[CONTENT_PREF_KEY] ?: ""
        }

    suspend fun updateContent(content: String) {
        getContext().dataStore.edit {
            it[CONTENT_PREF_KEY] = content
        }
    }
}