package com.bangkit.paras.di

import android.content.Context
import com.bangkit.paras.data.Repository
import com.bangkit.paras.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): Repository {
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(apiService)
    }
}