package com.bangkit.paras.data.remote.retrofit

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
        @Part file: MultipartBody.Part,
    ): ScanResponse

    @Multipart
    @POST("login")
    suspend fun login(
        @Part("username") username: RequestBody,
        @Part("password") password: RequestBody
    ) : UserResponse

    @Multipart
    @POST("register")
    suspend fun register(
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody
    ) : UserResponse

    @GET("account/{id}")
    suspend fun getProfile(
        @Header("Authorization")auth:String,
        @Path("id")username: String
    ) :ProfileResponse
}