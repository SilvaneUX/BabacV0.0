package com.smart.smartbalibackpaker.core.data.source.local.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ListRegistPlaceEntity(
    var place: String
) : Parcelable