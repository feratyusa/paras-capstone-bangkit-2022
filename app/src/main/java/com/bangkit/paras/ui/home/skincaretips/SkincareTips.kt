package com.bangkit.paras.ui.home.skincaretips

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SkincareTips(

    @field:SerializedName("SkincareTips")
    val skincareTips: List<SkincareTipsItem>,
) : Parcelable

@Parcelize
data class SkincareTipsItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("source")
    val source: String,

    @field:SerializedName("date")
    val date: String,

    @field:SerializedName("tips")
    val tips: List<TipsItem>,
) : Parcelable

@Parcelize
data class TipsItem(

    @field:SerializedName("no")
    val no: String,

    @field:SerializedName("tipstitle")
    val tipstitle: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("image")
    val image: String,
) : Parcelable
