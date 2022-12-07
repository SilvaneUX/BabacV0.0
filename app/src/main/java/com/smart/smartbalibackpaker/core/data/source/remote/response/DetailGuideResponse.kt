package com.smart.smartbalibackpaker.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class DetailGuideResponse(

	@field:SerializedName("list_perjalanan")
	val listPerjalanan: List<ListPerjalananItem?>? = null,

	@field:SerializedName("no_hp")
	val noHp: Any? = null,

	@field:SerializedName("id_backpacker")
	val idBackpacker: String? = null,

	@field:SerializedName("jumlah_perjalanan")
	val jumlahPerjalanan: Int? = null
)

data class ListPerjalananItem(

	@field:SerializedName("tgl_perjalanan")
	val tglPerjalanan: String? = null,

	@field:SerializedName("id_tempat_wisata")
	val idTempatWisata: List<Int?>? = null,

	@field:SerializedName("id_perjalanan")
	val idPerjalanan: Int? = null,

	@field:SerializedName("id_akomodasi")
	val idAkomodasi: Any? = null,

	@field:SerializedName("tgl_pulang")
	val tglPulang: String? = null
)
