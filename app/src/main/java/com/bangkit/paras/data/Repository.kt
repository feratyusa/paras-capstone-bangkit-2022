package com.bangkit.paras.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.paras.data.remote.response.ScanResponse
import com.bangkit.paras.data.remote.response.UserResponse
import com.bangkit.paras.data.remote.retrofit.ApiConfig
import com.bangkit.paras.data.remote.retrofit.ApiService
import com.bangkit.paras.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Repository(
    private val apiService: ApiService
) {

    fun uploadScan(getFile: File?): LiveData<Result<ScanResponse>> = liveData{
        emit(Result.Loading)
        try {
            if(getFile!=null){
                val file = reduceFileImage(getFile)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestImageFile
                )
                val apiService : ApiService = ApiConfig.getApiService()
                val service = apiService.uploadScan(imageMultipart)
                emit(Result.Success(service))
            }
        } catch (e: Exception) {
            Log.d("Repository", "uploadScan: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun login(username: String, password: String): LiveData<Result<UserResponse>> = liveData{
        emit(Result.Loading)
        try {
            val requestUsername = username.toRequestBody("text/plain".toMediaType())
            val requestPassword = password.toRequestBody("text/plain".toMediaType())
            val apiService : ApiService = ApiConfig.getApiService()
            val service = apiService.login(requestUsername, requestPassword)
            emit(Result.Success(service))

        } catch (e: Exception) {
            Log.d("Repository", "login: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(username: String, email: String, password: String): LiveData<Result<UserResponse>> = liveData{
        emit(Result.Loading)
        try {
            val requestUsername = username.toRequestBody("text/plain".toMediaType())
            val requestEmail = email.toRequestBody("text/plain".toMediaType())
            val requestPassword = password.toRequestBody("text/plain".toMediaType())
            val apiService : ApiService = ApiConfig.getApiService()
            val service = apiService.register(requestUsername, requestEmail, requestPassword)
            emit(Result.Success(service))

        } catch (e: Exception) {
            Log.d("Repository", "register: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}