package com.smart.smartbalibackpaker.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailTourismResponse(

	@field:SerializedName("place")
	val place: Place? = null,

	@field:SerializedName("tags")
	val tags: List<TagsItem?>? = null
)

data class Pivot(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("tag_id")
	val tagId: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null
)

data class Place(

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

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("slug")
	val slug: String? = null,

	@field:SerializedName("pictures")
	val pictures: List<Any?>? = null,

	@field:SerializedName("desc")
	val desc: String? = null,

	@field:SerializedName("price")
	val price: Int? = null
)

data class TagsItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("pivot")
	val pivot: Pivot? = null,

	@field:SerializedName("id")
	val id: Int? = null
)
