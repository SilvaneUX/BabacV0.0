package com.smart.smartbalibackpaker.core.model

import android.os.Parcelable
import java.io.Serializable

data class DetailPlaceGuide(
    var id: Int? = null,
    var thumbnail: String? = null,
    var address: String? = null,
    var updatedAt: String? = null,
    var latitude: String? = null,
    var longtitude: String? = null,
    var price: Int? = 0,
    var createdAt: String? = null,
    var title: String? = null,
    var type: String? = null,
    var slug: Double? = 0.0,
    var desc: String? = null,
    var isFavorite: Boolean = false,
//    var pictures: List<String?>? = null,
) : Serializable