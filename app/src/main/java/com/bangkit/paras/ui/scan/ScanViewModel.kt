package com.bangkit.paras.ui.scan

import androidx.lifecycle.ViewModel
import com.bangkit.paras.data.Repository
import java.io.File

class ScanViewModel(private val repository: Repository) : ViewModel() {
    fun uploadScan(file: File?) = repository.uploadScan(file)
}