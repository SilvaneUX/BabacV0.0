package com.smart.smartbalibackpaker.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class TourismResponse(

    @field:SerializedName("input")
    val input: List<Any?>? = null,

    @field:SerializedName("recordsFiltered")
    val recordsFiltered: Int? = null,

    @field:SerializedName("data")
    val data: List<DataItem?>? = null,

    @field:SerializedName("draw")
    val draw: Int? = null,

    @field:SerializedName("recordsTotal")
    val recordsTotal: Int? = null
)

data class DataItem(

    @field:SerializedName("thumbnail")
    val thumbnail: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("latitude")
    val latitude: String? = null,

    @field:SerializedName("longtitude")
    val longtitude: String? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("title")
    val title: String? = null,

//    @field:SerializedName("type")
//    val type: String? = null,

    @field:SerializedName("slug")
    val slug: String? = null,

    @field:SerializedName("desc")
    val desc: String? = null,

    @field:SerializedName("price")
    val price: Int? = null
)
