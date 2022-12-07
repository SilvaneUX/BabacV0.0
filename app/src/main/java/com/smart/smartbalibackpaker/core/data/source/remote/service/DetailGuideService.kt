package com.smart.smartbalibackpaker.core.data.source.remote.service

import com.smart.smartbalibackpaker.core.data.source.remote.response.DetailGuideResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailGuideService {

    @GET("record/{backpacker_id}")
    fun getRecordGuide(
        @Path("backpacker_id") backpackerId: String
    ) : Call<DetailGuideResponse>

}