package com.example.demo.model

import android.os.Parcel
import android.os.Parcelable

class ProductItem(
    val id: Int, val title: String, val price: Double, val description: String, val category: String, val image: String, val count: Int, val rate: Double, var isFavourite: Int, val position:Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(), parcel.readString() ?: "", parcel.readDouble(), parcel.readString() ?: "", parcel.readString() ?: "", parcel.readString() ?: "", parcel.readInt(), parcel.readDouble(),parcel.readInt(),parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeDouble(price)
        parcel.writeString(description)
        parcel.writeString(category)
        parcel.writeString(image)
        parcel.writeInt(count)
        parcel.writeDouble(rate)
        parcel.writeInt(isFavourite)
        parcel.writeInt(position)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductItem> {
        override fun createFromParcel(parcel: Parcel): ProductItem {
            return ProductItem(parcel)
        }

        override fun newArray(size: Int): Array<ProductItem?> {
            return arrayOfNulls(size)
        }
    }
}
