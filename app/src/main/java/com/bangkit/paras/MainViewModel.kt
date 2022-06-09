package com.bangkit.paras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.paras.data.Repository
import com.bangkit.paras.data.remote.response.Credentials
import com.bangkit.paras.di.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    private val repository: Repository,
) : ViewModel() {

    private lateinit var credentials: Credentials

    init {
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
            credentials = dataStoreManager.getUser().first()
        }
    }

    //history
    fun getUserHistory() = repository.getUserHistory(credentials)

    //profile
    fun getUserProfile() = repository.getUserProfile(credentials)
    fun editUserProfileEmail(email: String) = repository.editUserProfileEmail(email, credentials)
    fun editUserProfilePhone(phone: String) = repository.editUserProfilePhone(phone, credentials)
    fun editUserProfilePhoto(photo: File?) = repository.editUserProfilePhoto(photo, credentials)
}