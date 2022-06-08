package com.bangkit.paras.data.remote.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("handphone")
    val handphone: String? = null,

    @field:SerializedName("photo")
    val photo: String? = null,

    @field:SerializedName("historyCount")
    val historyCount: Int? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("username")
    val username: String? = null,
)
