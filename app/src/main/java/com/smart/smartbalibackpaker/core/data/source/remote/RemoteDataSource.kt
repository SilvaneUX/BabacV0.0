package com.smart.smartbalibackpaker.core.data.source.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity
import com.smart.smartbalibackpaker.core.data.source.remote.response.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDataSource {

    fun getAllTourism(placeType: String): LiveData<ApiResponse<List<DataItem>>> {
        val listPlaces = MutableLiveData<ApiResponse<List<DataItem>>>()
//        EspressoIdlingResource.increment()
        ConfigNetwork.getRetrofit().getAllTourism(placeType).enqueue(object : Callback<TourismResponse> {
            override fun onResponse(call: Call<TourismResponse>, response: Response<TourismResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()?.data
                    listPlaces.postValue(ApiResponse.success(body as List<DataItem>))
//                    EspressoIdlingResource.decrement()
                    Log.d("responseplace1", body.toString())

                }
            }

            override fun onFailure(call: Call<TourismResponse>, t: Throwable) {
                Log.d("Response", "On Failure")
            }
        })
        Log.d("responseplace2", listPlaces.value?.body?.toString() ?: "")
        return listPlaces
    }

    fun getDetailTourism(placeId: Int): LiveData<ApiResponse<Place>> {
        val listDetailTourism = MutableLiveData<ApiResponse<Place>>()
//        EspressoIdlingResource.increment()
        ConfigNetwork.getRetrofit().getDetailTourism(placeId)
            .enqueue(object : Callback<Place?> {
                override fun onResponse(
                    call: Call<Place?>, response: Response<Place?>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        listDetailTourism.postValue(ApiResponse.success(body as Place))
//                        EspressoIdlingResource.decrement()
                    }
                }

                override fun onFailure(call: Call<Place?>, t: Throwable) {
                    Log.d("Response", "On Failure")
                }
            })
        return listDetailTourism
    }

    fun getUploadResult(email: String?,
                        nama: String?,
                        noHp: String?,
                        tglDatang: String?,
                        tglPergi: String?,
                        tmpDatang: String?,
                        akomodasi: String?,
                        hotel: String?,
                        tmpWisata: String?): LiveData<ApiResponse<ResponseAction>> {
        val listDetails = MutableLiveData<ApiResponse<ResponseAction>>()
        ConfigNetwork.getRetrofit().addRecordBackpacker(
            email ?: "",
//            nama ?: "",
            noHp ?: "",
            tmpDatang ?: "",
            tglDatang ?: "",
            tglPergi ?: "",
            tmpWisata?: "",
            hotel ?: "",
            akomodasi ?: ""
            ).enqueue(object : Callback<ResponseAction> {
            override fun onFailure(call: Call<ResponseAction>, t: Throwable) {
                val data = ResponseAction(
                    message = "Failed",
                    isSuccess = false
                )
//                    BackpackerRequest(
//                    email = "email",
//                    nama = "nama",
//                    noHp = "hp",
//                    tglDatang = "tgldtg",
//                    tglPergi = "tglper",
//                    tmpDatang = "tmpdata",
//                    akomodasi = "acc",
//                    hotel = "hotel",
//                    tmpWisata = "tmpwisata"
//                )
                listDetails.postValue(ApiResponse.success(data))
            }

            override fun onResponse(
                call: Call<ResponseAction>,
                response: Response<ResponseAction>
            ) {
                if (response.isSuccessful) {
                    val result = response.body()
                    val data = ResponseAction(
                        message = "Data berhasil disimpan",
                        isSuccess = true
                    )
                    listDetails.postValue(ApiResponse.success(data))
//                    if (body.toString() == "[]") {
//                        Toast.makeText(context, "Data not found", Toast.LENGTH_SHORT).show()
//                    }
                }
            }
        })
        return listDetails
    }

    fun getRecordBackpacker(backpackerId: String): LiveData<ApiResponse<DetailGuideResponse>>{
        val mutableRecord = MutableLiveData<ApiResponse<DetailGuideResponse>>()

        ConfigNetwork.getRecordNetwork().getRecordGuide(backpackerId).enqueue(object : Callback<DetailGuideResponse>{
            override fun onResponse(
                call: Call<DetailGuideResponse>,
                response: Response<DetailGuideResponse>
            ) {
                if(response.isSuccessful){
                    val body = response.body()
                    mutableRecord.postValue(ApiResponse.success(body as DetailGuideResponse))
                    Log.d("responseguide", body.listPerjalanan?.toString() ?: " ")

                }
            }

            override fun onFailure(call: Call<DetailGuideResponse>, t: Throwable) {
            }
        })

        return mutableRecord
    }

    fun getAccom(): LiveData<ApiResponse<List<DataItemAllAccom>>> {
        val listPlaces = MutableLiveData<ApiResponse<List<DataItemAllAccom>>>()
        ConfigNetwork.getRetrofit().getAccom().enqueue(object : Callback<AllAccomResponse> {
            override fun onResponse(call: Call<AllAccomResponse>, response: Response<AllAccomResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()?.data
                    listPlaces.postValue(ApiResponse.success(body as List<DataItemAllAccom>))
                }
            }

            override fun onFailure(call: Call<AllAccomResponse>, t: Throwable) {
                Log.d("Response", t.toString())
            }
        })
        return listPlaces
    }

    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource().apply { instance = this }
            }
    }
}