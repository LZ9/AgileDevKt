package com.lodz.android.agiledevkt.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Parcelable测试类
 * @author zhouL
 * @date 2021/11/1
 */
class ParcelableBean(var name: String?) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ParcelableBean> {
        override fun createFromParcel(parcel: Parcel): ParcelableBean {
            return ParcelableBean(parcel)
        }

        override fun newArray(size: Int): Array<ParcelableBean?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return name ?: "null"
    }
}