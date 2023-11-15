package com.example.tubes1.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Image(
    val uri: String,
    var name: String,
    var date: String,
    var description: String,
    var story: String
) : Parcelable{

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(uri)
        parcel.writeString(name)
        parcel.writeString(date)
        parcel.writeString(description)
        parcel.writeString(story)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }

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




