package com.example.tubes1.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Image(
    val path: String,
    val name: String,
    val date: Date,
    val description: String,
    val story: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        Date(parcel.readLong()),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun equals(other: Any?): Boolean {
        return if (other is Image) {
            this.path == other.path
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(path)
        parcel.writeString(name)
        parcel.writeLong(date.time)
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
}



