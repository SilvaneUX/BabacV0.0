package com.smart.smartbalibackpaker.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class BackpackerRequest(
    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("noHp")
    val noHp: String? = null,

    @field:SerializedName("tglDatang")
    val tglDatang: String? = null,

    @field:SerializedName("tglPergi")
    val tglPergi: String? = null,

    @field:SerializedName("tmpDatang")
    val tmpDatang: String? = null,

    @field:SerializedName("akomodasi")
    val akomodasi: String? = null,

    @field:SerializedName("hotel")
    val hotel: String? = null,

    @field:SerializedName("tmpWisata")
    val tmpWisata: String? = null,
)
