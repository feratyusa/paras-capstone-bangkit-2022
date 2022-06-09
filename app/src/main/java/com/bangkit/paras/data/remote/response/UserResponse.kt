package com.bangkit.paras.data.remote.response

import com.google.gson.annotations.SerializedName

data class UserResponse(

    @field:SerializedName("credentials")
    val credentials: Credentials? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("error")
    val error: String,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("statusCode")
    val statusCode: Int,
)

data class Credentials(

    @field:SerializedName("authorization")
    val authorization: String? = null,

    @field:SerializedName("username")
    val username: String? = null,
)