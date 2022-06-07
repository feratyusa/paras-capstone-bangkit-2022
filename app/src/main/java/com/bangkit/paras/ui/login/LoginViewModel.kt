package com.bangkit.paras.ui.login

import androidx.lifecycle.ViewModel
import com.bangkit.paras.data.Repository

class LoginViewModel(private val repository: Repository) : ViewModel() {
    fun login(username: String, password: String) = repository.login(username, password)
}