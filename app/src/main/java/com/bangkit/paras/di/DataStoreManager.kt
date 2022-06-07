package com.bangkit.paras.di

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.paras.data.remote.response.Credentials
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore("settings")


@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val userDataStore = appContext.dataStore

    private var PREF_UNAME = stringPreferencesKey("username")
    private var PREF_AUTHORIZATION = stringPreferencesKey("authorization")

    suspend fun setUser(credentials: Credentials?) {
        userDataStore.edit { data ->
            if (credentials != null) {
                data[PREF_UNAME] = credentials.username.toString()
                data[PREF_AUTHORIZATION] = credentials.authorization.toString()
            }
        }
    }

    fun getUser(): Flow<Credentials> {
        val user = userDataStore.data
            .map { preferences ->
                Credentials(
                    authorization = preferences[PREF_AUTHORIZATION] ?: "",
                    username = preferences[PREF_UNAME] ?: ""
                )
            }
        return user
    }

}
