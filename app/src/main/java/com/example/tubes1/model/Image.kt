package com.example.tubes1.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Image(
    val uri: String,
    var name: String,
    var date: String,
    var story: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Image

        return uri == other.uri
    }

    override fun hashCode(): Int {
        return uri.hashCode()
    }
}




