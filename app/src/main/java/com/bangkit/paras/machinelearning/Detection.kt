package com.bangkit.paras.machinelearning

import android.graphics.Bitmap

data class Detection(
    val confidence: Float,
    val bitmap: Bitmap
)