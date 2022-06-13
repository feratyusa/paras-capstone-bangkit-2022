package com.bangkit.paras.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanResponse(

    @field:SerializedName("date")
    val date: String? = null,

    @field:SerializedName("symptom")
    val symptom: String? = null,

    @field:SerializedName("id")
    val id: String? = null,
) : Parcelable
