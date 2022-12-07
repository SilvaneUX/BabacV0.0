package com.smart.smartbalibackpaker.core.data.source.remote.service

import com.smart.smartbalibackpaker.core.data.source.remote.response.*
import retrofit2.Call
import retrofit2.http.*

interface TourismService {

    @GET("{type_place}")
    fun getAllTourism(
        @Path("type_place") type_place: String
    ): Call<TourismResponse>

    @GET("hotel")
    fun getAllHotel(): Call<TourismResponse>

    @GET("tempat-ibadah")
    fun getAllWorship(): Call<TourismResponse>

    @GET("place/{id_place}")
    fun getDetailTourism(
        @Path("id_place") id_place: Int
    ): Call<Place>

    //    @Headers("Content-Type: application/json")
//    @FormUrlEncoded
//    @POST("add/backpacker")
//    fun addRecordBackpacker(
//        @Field("email") email: String,
//        @Field("nama") nama: String,
//        @Field("noHp") noHp: String,
//        @Field("tglDatang") tglDatang: String,
//        @Field("tglPergi") tglPergi: String,
//        @Field("tmpDatang") tmpDatang: String,
//        @Field("akomodasi") akomodasi: String,
//        @Field("hotel") hotel: String,
//        @Field("tmpWisata") tmpWisata: String,
//    ): Call<ResponseAction>

    @FormUrlEncoded
    @POST("add/record")
    fun addRecordBackpacker(
        @Field("backpacker_id") backpacker_id: String,
//        @Field("nama") nama: String,
        @Field("noHp") noHp: String,
        @Field("tmpDatang") tmpDatang: String,
        @Field("tglDatang") tglDatang: String,
        @Field("tglPergi") tglPergi: String,
        @Field("tmpWisata") tmpWisata: String,
        @Field("hotel") hotel: String,
        @Field("akomodasi") akomodasi: String,
    ): Call<ResponseAction>

    @GET("all/mobil")
    fun getAccom(
    ): Call<AllAccomResponse>

}