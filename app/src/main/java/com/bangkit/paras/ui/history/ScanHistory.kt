package com.bangkit.paras.ui.history

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScanHistory(

    @field:SerializedName("Scan")
    val scan: List<ScanItem>
) : Parcelable

@Parcelize
data class ScanItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("date")
    val date: String,

) : Parcelable

