package com.bangkit.paras

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.bangkit.paras.data.Repository
import com.bangkit.paras.data.remote.response.Credentials
import com.bangkit.paras.di.DataStoreManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val dataStoreManager: DataStoreManager,
    private val repository: Repository
):ViewModel(){

    private lateinit var credentials:Credentials

    init {
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
            credentials=dataStoreManager.getUser().first()
        }
    }

    fun getUserProfile()=repository.getUserProfile(credentials)

}