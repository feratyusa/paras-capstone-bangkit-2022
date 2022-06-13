package com.bangkit.paras.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.paras.data.remote.response.*
import com.bangkit.paras.data.remote.retrofit.ApiService
import com.bangkit.paras.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class Repository(
    private val apiService: ApiService,
) {

    fun uploadScan(getFile: File?, credentials: Credentials): LiveData<Result<ScanResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                if (getFile != null) {
                    val file = reduceFileImage(getFile)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        requestImageFile
                    )
                    val service =
                        apiService.uploadScan("Basic ${credentials.authorization.toString()}",
                            imageMultipart)
                    emit(Result.Success(service))
                }
            } catch (e: Exception) {
                Log.d("Repository", "uploadScan: ${e.message.toString()} ")
                emit(Result.Error(e.message.toString()))
            }
        }

    fun login(username: String, password: String): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val requestUsername = username.toRequestBody("text/plain".toMediaType())
            val requestPassword = password.toRequestBody("text/plain".toMediaType())
            val service = apiService.login(requestUsername, requestPassword)
            emit(Result.Success(service))

        } catch (e: Exception) {
            if (e.message.toString() == "HTTP 409 ") {
                emit(Result.Error("Username doesn't exist or password is wrong"))
            } else if (e.message.toString() == "HTTP 400 ") {
                emit(Result.Error("Username doesn't exist or password is wrong"))
            } else {
                emit(Result.Error("Something went wrong"))
            }
            Log.d("Repository", "login: ${e.message.toString()} ")
        }
    }

    fun register(
        username: String,
        email: String,
        password: String,
    ): LiveData<Result<UserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val requestUsername = username.toRequestBody("text/plain".toMediaType())
            val requestEmail = email.toRequestBody("text/plain".toMediaType())
            val requestPassword = password.toRequestBody("text/plain".toMediaType())
            val service = apiService.register(requestUsername, requestEmail, requestPassword)
            emit(Result.Success(service))

        } catch (e: Exception) {
            if (e.message.toString() == "HTTP 409 ") {
                emit(Result.Error("Username already exist"))
            } else if (e.message.toString() == "HTTP 400 ") {
                emit(Result.Error("Bad Request"))
            } else {
                emit(Result.Error("Something went wrong"))
            }
            Log.d("Repository", "register: ${e.message.toString()} ")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getUserProfile(credentials: Credentials): LiveData<Result<ProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            val service = apiService.getProfile(username = credentials.username.toString(),
                auth = "Basic ${credentials.authorization.toString()}")
            emit(Result.Success(service))

        } catch (e: Exception) {
            if (e.message.toString() == "HTTP 409 ") {
                emit(Result.Error("Username doesn't exist or password is wrong"))
            } else if (e.message.toString() == "HTTP 400 ") {
                emit(Result.Error("Bad Request"))
            } else {
                emit(Result.Error("Something went wrong"))
            }
            Log.d("Repository", "getUserProfile: ${e.message.toString()} ")
        }
    }

    fun editUserProfileEmail(
        email: String,
        credentials: Credentials,
    ): LiveData<Result<ProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            val requestEmail = email.toRequestBody("text/plain".toMediaType())
            val service = apiService.editProfileEmail(username = credentials.username.toString(),
                auth = "Basic ${credentials.authorization.toString()}",
                email = requestEmail)
            emit(Result.Success(service))

        } catch (e: Exception) {
            if (e.message.toString() == "HTTP 409 ") {
                emit(Result.Error("Username doesn't exist or password is wrong"))
            } else if (e.message.toString() == "HTTP 400 ") {
                emit(Result.Error("Bad Request"))
            } else {
                emit(Result.Error("Something went wrong"))
            }
            Log.d("Repository", "getUserProfile: ${e.message.toString()} ")
        }
    }

    fun editUserProfilePhone(
        phone: String,
        credentials: Credentials,
    ): LiveData<Result<ProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            val requestPhone = phone.toRequestBody("text/plain".toMediaType())
            val service = apiService.editProfilePhone(username = credentials.username.toString(),
                auth = "Basic ${credentials.authorization.toString()}",
                handphone = requestPhone)
            emit(Result.Success(service))

        } catch (e: Exception) {
            if (e.message.toString() == "HTTP 409 ") {
                emit(Result.Error("Username doesn't exist or password is wrong"))
            } else if (e.message.toString() == "HTTP 400 ") {
                emit(Result.Error("Bad Request"))
            } else {
                emit(Result.Error("Something went wrong"))
            }
            Log.d("Repository", "getUserProfile: ${e.message.toString()} ")
        }
    }

    fun editUserProfilePhoto(
        photo: File?,
        credentials: Credentials,
    ): LiveData<Result<ProfileResponse>> = liveData {
        emit(Result.Loading)
        try {
            if (photo != null) {
                val file = reduceFileImage(photo)
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                val service =
                    apiService.editProfilePhoto(username = credentials.username.toString(),
                        auth = "Basic ${credentials.authorization.toString()}",
                        photo = imageMultipart)
                emit(Result.Success(service))
            }

        } catch (e: Exception) {
            if (e.message.toString() == "HTTP 409 ") {
                emit(Result.Error("Username doesn't exist or password is wrong"))
            } else if (e.message.toString() == "HTTP 400 ") {
                emit(Result.Error("Bad Request"))
            } else {
                emit(Result.Error("Something went wrong"))
            }
            Log.d("Repository", "getUserProfile: ${e.message.toString()} ")
        }
    }

    fun getUserHistory(credentials: Credentials): LiveData<Result<List<HistoryResponse>>> =
        liveData {
            emit(Result.Loading)
            try {
                val service = apiService.getHistory(username = credentials.username.toString(),
                    auth = "Basic ${credentials.authorization.toString()}")
                emit(Result.Success(service))

            } catch (e: Exception) {
                if (e.message.toString() == "HTTP 409 ") {
                    emit(Result.Error("Username doesn't exist or password is wrong"))
                } else if (e.message.toString() == "HTTP 400 ") {
                    emit(Result.Error("Bad Request"))
                } else {
                    emit(Result.Error("Something went wrong"))
                }
                Log.d("Repository", "getUserHistory: ${e.message.toString()} ")
            }
        }


    companion object {
        @Volatile
        private var instance: Repository? = null
        fun getInstance(
            apiService: ApiService,
        ): Repository =
            instance ?: synchronized(this) {
                instance ?: Repository(apiService)
            }.also { instance = it }
    }
}