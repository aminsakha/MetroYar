package com.metroyar.db

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first

suspend fun <T> read(dataStore: DataStore<Preferences>, key: Preferences.Key<T>): T? {
    val preferences = dataStore.data.first()
    return preferences[key]
}


suspend fun <T> save(dataStore: DataStore<Preferences>, key: Preferences.Key<T>, value: T) {
    dataStore.edit { settings ->
        settings[key] = value
    }
}

object PreferencesKeys {
    val SHOULD_VIBRATE_PHONE = booleanPreferencesKey("shouldVibratePhone")
    val SHOULD_PLAY_SOUND = booleanPreferencesKey("shouldPlaySound")
    val FAVORITE_STATIONS = stringPreferencesKey("favoriteStations")
}