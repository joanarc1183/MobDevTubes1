package com.example.tubes1.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Image(
    var uri: String,
    val name: String,
    val date: String,
    val story: String,
)



