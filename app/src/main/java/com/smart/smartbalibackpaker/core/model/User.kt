package com.smart.smartbalibackpaker.core.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val username: String = "",
    val email: String = "",
    val password: String = "",
) : Parcelable