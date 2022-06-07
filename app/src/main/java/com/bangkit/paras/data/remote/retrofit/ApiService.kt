package com.bangkit.paras.data.remote.retrofit

import com.bangkit.paras.data.remote.response.HistoryResponse
import com.bangkit.paras.data.remote.response.ProfileResponse
import com.bangkit.paras.data.remote.response.ScanResponse
import com.bangkit.paras.data.remote.response.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("predict-photo")
    suspend fun uploadScan(
        @Header("Authorization")auth:String,
        @Part file: MultipartBody.Part
    ): ScanResponse

    @Multipart
    @POST("login")
    suspend fun login(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody,
    ): UserResponse

    @Multipart
    @POST("register")
    suspend fun register(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
    ): UserResponse

    @GET("account/{id}")
    suspend fun getProfile(
        @Header("Authorization") auth: String,
        @Path("id") username: String,
    ): ProfileResponse

    @Multipart
    @POST("account/{id}/edit")
    suspend fun editProfileEmail(
        @Header("Authorization") auth: String,
        @Path("id") username: String,
        @Part("email") email: RequestBody,
    ): ProfileResponse

    @Multipart
    @POST("account/{id}/edit")
    suspend fun editProfilePhone(
        @Header("Authorization") auth: String,
        @Path("id") username: String,
        @Part("handphone") handphone: RequestBody,
    ): ProfileResponse

    @Multipart
    @POST("account/{id}/edit")
    suspend fun editProfilePhoto(
        @Header("Authorization") auth: String,
        @Path("id") username: String,
        @Part photo: MultipartBody.Part,
    ): ProfileResponse

    @GET("history/{id}")
    suspend fun getHistory(
        @Header("Authorization") auth: String,
        @Path("id") username: String,
    ): List<HistoryResponse>
}