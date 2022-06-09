package com.bangkit.paras.data.remote.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("symptom")
    val symptom: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("image")
    val image: String,
)
