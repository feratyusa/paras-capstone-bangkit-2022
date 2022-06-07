package com.bangkit.paras.ui.register

import androidx.lifecycle.ViewModel
import com.bangkit.paras.data.Repository

class RegisterViewModel(private val repository: Repository) : ViewModel() {
    fun register(username: String, email: String, password: String) =
        repository.register(username, email, password)
}