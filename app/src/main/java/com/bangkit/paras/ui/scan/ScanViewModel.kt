package com.bangkit.paras.ui.scan

import android.util.Log
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
class ScanViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStoreManager: DataStoreManager
): ViewModel() {
    private lateinit var credentials: Credentials

    init {
        viewModelScope.launch {
            // Coroutine that will be canceled when the ViewModel is cleared.
            credentials=dataStoreManager.getUser().first()
            Log.d("viewmodelscan", "$credentials: ")
        }
    }

    fun uploadScan(file : File?) = repository.uploadScan(credentials = credentials,getFile=file)
}