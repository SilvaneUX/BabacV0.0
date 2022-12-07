package com.smart.smartbalibackpaker.core.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.smart.smartbalibackpaker.core.data.source.local.entity.*
import com.smart.smartbalibackpaker.core.data.source.remote.ApiResponse
import com.smart.smartbalibackpaker.core.data.source.remote.RemoteDataSource
import com.smart.smartbalibackpaker.core.data.source.remote.response.*
import com.smart.smartbalibackpaker.core.utils.AppExecutors
import com.smart.smartbalibackpaker.core.utils.Coordinate
import com.smart.smartbalibackpaker.core.utils.distance
import com.smart.smartbalibackpaker.core.vo.Resource
import java.util.*
import kotlin.collections.ArrayList

class TourismRepository private constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val appExecutors: AppExecutors
) :
    TourismDataSource, GuideDataSource, RecordGuideDataSource {

//    private fun actionGetRates(name: String, keyword: String, location: String, key: String) {
//        ConfigNetwork
//            .getPlacesNetwork().searchNearby(keyword,location,key)
//            .enqueue(object: Callback<NearbySearchResponse> {
//                override fun onResponse(
//                    call: Call<NearbySearchResponse>,
//                    response: Response<NearbySearchResponse>
//                ) {
//                    if(response.isSuccessful){
//                        val placeData = response.body()?.results?.get(0)
//                        val placeRate = placeData?.rating.toString()
//                        val placeRateCount = placeData?.userRatingsTotal.toString()
//
//
//
//                    }
//                }
//
//                override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
//                }
//            })
//    }


    override fun getAllTourism(placeType: String): LiveData<Resource<PagedList<TourismDataEntity>>> {
        return object :
            NetworkBoundResource<PagedList<TourismDataEntity>, List<DataItem>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<TourismDataEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(localDataSource.getPlaces(placeType), config).build()
            }

            override fun shouldFetch(data: PagedList<TourismDataEntity>?): Boolean =
//                data == null || data.isEmpty()
                true
            override fun createCall(): LiveData<ApiResponse<List<DataItem>>> {
                return when (placeType) {
                    "tour" -> {
                        remoteDataSource.getAllTourism("wisata")
                    }
                    "hotel" -> {
                        remoteDataSource.getAllTourism("hotel")
                    }
                    "worship" -> {
                        remoteDataSource.getAllTourism("tempat-ibadah")
                    }
                    //prepare for getALlWisata
                    else -> remoteDataSource.getAllTourism("wisata")
                }
            }

            override fun saveCallResult(data: List<DataItem>) {
                val placeList = ArrayList<TourismDataEntity>()
                for (response in data) {
                    val movie = TourismDataEntity(
                        id = response.id,
                        thumbnail = response.thumbnail,
                        address = response.address,
                        latitude = response.latitude,
                        longtitude = response.longtitude,
                        title = response.title,
                        type = placeType,
                        slug = 0.0,
                        desc = response.desc,
                        price = response.price
                    )
                    placeList.add(movie)
                    Log.d("DATAMOVIE", movie.toString())
                }
                localDataSource.insertPlace(placeList)
            }
        }.asLiveData()
    }

    override fun getChooseAllTourism(placeType: String, lat1: String, long1: String, sort: String, priceTarget: Int): LiveData<Resource<PagedList<TourismDataEntity>>> {
        return object :
            NetworkBoundResource<PagedList<TourismDataEntity>, List<DataItem>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<TourismDataEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(localDataSource.getChoosePlaces(placeType, sort, priceTarget), config).build()
            }

            override fun shouldFetch(data: PagedList<TourismDataEntity>?): Boolean =
//                data == null || data.isEmpty()
                true
            override fun createCall(): LiveData<ApiResponse<List<DataItem>>> {
                return when (placeType) {
                    "tour" -> {
                        remoteDataSource.getAllTourism("wisata")
                    }
//                    "hotel" -> {
//                        remoteDataSource.getAllTourism("hotel")
//                    }
                    "worship" -> {
                        remoteDataSource.getAllTourism("tempat-ibadah")
                    }
                    //prepare for getALlWisata
                    else -> remoteDataSource.getAllTourism("wisata")
                }
            }

            override fun saveCallResult(data: List<DataItem>) {
                val placeList = ArrayList<TourismDataEntity>()
                val arrivalPlace = Coordinate(lat1.toDouble(), long1.toDouble())
                when (sort) {
                    "priceTarget" -> {
                        var priceAlloc = 0
                        for (response in data) {
                            var placeRate = ""
                            // Haversine Formulating
                            val destNode = Coordinate(response.latitude?.toDouble()!!, response.longtitude?.toDouble()!!)
                            val resultDist = distance(arrivalPlace, destNode)

                            val apiKeyGMaps = "AIzaSyBfUKBEp3aNBuwVER3OhK3hWBiCV0DKzgM"

                            //                    //dev only
                            //                    val rands = (0..10).random()
                            //                    val inIdr = "000"
                            //                    val priceDev = "$rands$inIdr"
                            //                    val priceData = priceDev.toInt()

                            //                        ConfigNetwork
                            //                            .getPlacesNetwork().searchNearby("${response.title}", "${response.latitude},${response.longtitude}","500",apiKeyGMaps)
                            //                            .enqueue(object: Callback<NearbySearchResponse> {
                            //                                override fun onResponse(
                            //                                    call: Call<NearbySearchResponse>,
                            //                                    response: Response<NearbySearchResponse>
                            //                                ) {
                            //                                    if(response.isSuccessful){
                            //                                        val placeData = response.body()?.results?.firstOrNull { it?.rating != null }
                            ////                            val placeData = response.body()?.results?.firstNotNullOf { it?.rating.toString() }
                            //                                        placeRate = placeData?.rating.toString()
                            //                                    }
                            //                                }
                            //
                            //                                override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                            //                                    Log.d("DATADISTANCEEE", "GAGAL")
                            //                                }
                            //                            })
                            priceAlloc += response.price!!

                            if (priceAlloc < priceTarget){

                                val movie = TourismDataEntity(
                                    id = response.id,
                                    thumbnail = response.thumbnail,
                                    address = response.address,
                                    latitude = response.latitude,
                                    longtitude = response.longtitude,
                                    title = response.title,
                                    type = "tour",
                                    slug = resultDist.toDouble(),
                                    //                        desc = "$placeRate ($placeRateCount reviews)"
                                    desc = placeRate,
                                    price = response.price
                                )
                                Log.d("PRICEALLOC", "PRICE TIKET = ${movie.price} , PRICEALLOC =  $priceAlloc , PRICETARGET = $priceTarget")
                                //                        Log.d("PRICETARGET", priceTarget.toString())

                                placeList.add(movie)
                            }
                        }
                    }
                    // END IF
                    "price" -> {
                        Log.d("JENISSORT", sort.toString())
                        for (response in data) {

                            var placeRate = ""
                            // Haversine Formulating
                            val destNode = Coordinate(response.latitude?.toDouble()!!, response.longtitude?.toDouble()!!)
                            val resultDist = distance(arrivalPlace, destNode)

                            val apiKeyGMaps = "AIzaSyBfUKBEp3aNBuwVER3OhK3hWBiCV0DKzgM"

                            //                    //dev only
                            //                    val rands = (0..10).random()
                            //                    val inIdr = "000"
                            //                    val priceDev = "$rands$inIdr"
                            //                    val priceData = priceDev.toInt()

                            //                        ConfigNetwork
                            //                            .getPlacesNetwork().searchNearby("${response.title}", "${response.latitude},${response.longtitude}","500",apiKeyGMaps)
                            //                            .enqueue(object: Callback<NearbySearchResponse> {
                            //                                override fun onResponse(
                            //                                    call: Call<NearbySearchResponse>,
                            //                                    response: Response<NearbySearchResponse>
                            //                                ) {
                            //                                    if(response.isSuccessful){
                            //                                        val placeData = response.body()?.results?.firstOrNull { it?.rating != null }
                            ////                            val placeData = response.body()?.results?.firstNotNullOf { it?.rating.toString() }
                            //                                        placeRate = placeData?.rating.toString()
                            //                                    }
                            //                                }
                            //
                            //                                override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                            //                                    Log.d("DATADISTANCEEE", "GAGAL")
                            //                                }
                            //                            })

                            val movie = TourismDataEntity(
                                id = response.id,
                                thumbnail = response.thumbnail,
                                address = response.address,
                                latitude = response.latitude,
                                longtitude = response.longtitude,
                                title = response.title,
                                type = "tour",
                                slug = resultDist.toDouble(),
                                //                        desc = "$placeRate ($placeRateCount reviews)"
                                desc = placeRate,
                                price = response.price
                            )
                            placeList.add(movie)
                        }
                    }
                    "slug" -> {
                        Log.d("JENISSORT", sort.toString())
                        for (response in data) {

                            var placeRate = ""
                            // Haversine Formulating
                            val destNode = Coordinate(response.latitude?.toDouble()!!, response.longtitude?.toDouble()!!)
                            val resultDist = distance(arrivalPlace, destNode)

                            val apiKeyGMaps = "AIzaSyBfUKBEp3aNBuwVER3OhK3hWBiCV0DKzgM"

                            //                    //dev only
                            //                    val rands = (0..10).random()
                            //                    val inIdr = "000"
                            //                    val priceDev = "$rands$inIdr"
                            //                    val priceData = priceDev.toInt()

                            //                        ConfigNetwork
                            //                            .getPlacesNetwork().searchNearby("${response.title}", "${response.latitude},${response.longtitude}","500",apiKeyGMaps)
                            //                            .enqueue(object: Callback<NearbySearchResponse> {
                            //                                override fun onResponse(
                            //                                    call: Call<NearbySearchResponse>,
                            //                                    response: Response<NearbySearchResponse>
                            //                                ) {
                            //                                    if(response.isSuccessful){
                            //                                        val placeData = response.body()?.results?.firstOrNull { it?.rating != null }
                            ////                            val placeData = response.body()?.results?.firstNotNullOf { it?.rating.toString() }
                            //                                        placeRate = placeData?.rating.toString()
                            //                                    }
                            //                                }
                            //
                            //                                override fun onFailure(call: Call<NearbySearchResponse>, t: Throwable) {
                            //                                    Log.d("DATADISTANCEEE", "GAGAL")
                            //                                }
                            //                            })

                            val movie = TourismDataEntity(
                                id = response.id,
                                thumbnail = response.thumbnail,
                                address = response.address,
                                latitude = response.latitude,
                                longtitude = response.longtitude,
                                title = response.title,
                                type = "tour",
                                slug = resultDist.toDouble(),
                                //                        desc = "$placeRate ($placeRateCount reviews)"
                                desc = placeRate,
                                price = response.price
                            )
                            placeList.add(movie)
                        }
                    }
                }
//                placeList.sortBy { it.slug }
                localDataSource.insertPlace(placeList)
            }
        }.asLiveData()
    }

    override fun getDetailGuideTourism(placeId: Int): LiveData<TourismDataEntity> {
        return localDataSource.getDetailPlace(placeId)
    }

    override fun getDetailTourism(placeId: Int): LiveData<Resource<TourismDataEntity>> {
        return object : NetworkBoundResource<TourismDataEntity, Place>(appExecutors) {
            override fun loadFromDB(): LiveData<TourismDataEntity> {
                return localDataSource.getDetailPlace(placeId)
            }

            override fun shouldFetch(data: TourismDataEntity?): Boolean =
                data?.title.isNullOrEmpty()

            override fun createCall(): LiveData<ApiResponse<Place>> =
                remoteDataSource.getDetailTourism(placeId)

            override fun saveCallResult(data: Place) {
                val tourismDetail = TourismDataEntity(
                    id = data.id,
                    thumbnail = data.thumbnail,
                    address = data.address,
                    latitude = data.latitude,
                    longtitude = data.longtitude,
                    title = data.title,
                    type = data.type,
                    slug = data.slug?.toDouble(),
                    desc = data.desc,
                    price = data.price
                )
                localDataSource.updatePlace(tourismDetail)
            }
        }.asLiveData()
    }

    override fun deleteTourism(id: Int) {
            return localDataSource.deletePlace(id)
    }

    override fun uploadRecord(email: String?,
                              nama: String?,
                              noHp: String?,
                              tglDatang: String?,
                              tglpergi: String?,
                              tmpDatang: String?,
                              akomodasi: String?,
                              hotel: String?,
                              tmpWisata: String?): LiveData<Resource<UploadResultEntity>> {
        return object : NetworkBoundResource<UploadResultEntity, ResponseAction>(appExecutors){
            override fun loadFromDB(): LiveData<UploadResultEntity> {
                Log.d("UPLOAD LOADFROMDB", email.toString())
                return localDataSource.getUploadResult(tglDatang.toString())
            }

            override fun shouldFetch(data: UploadResultEntity?): Boolean =
                data?.message != tglDatang.toString()

            override fun createCall(): LiveData<ApiResponse<ResponseAction>> {
                Log.d("UPLOAD CREATECALL", "UDAH UPLOAD")
                return remoteDataSource.getUploadResult(
                    email,
                    nama,
                    noHp,
                    tglDatang,
                    tglpergi,
                    tmpDatang,
                    akomodasi,
                    hotel,
                    tmpWisata
                )
            }
            override fun saveCallResult(data: ResponseAction) {
                Log.d("UPLOAD SAVECALL", data.toString())
                val result = UploadResultEntity(
                    message = tglDatang,
                    isSuccess = true
                )
                Log.d("UPLOAD SAVECALL", result.toString())
                localDataSource.insertResult(result)
            }

        }.asLiveData()
    }

    override fun getAccom(): LiveData<Resource<PagedList<AccomDataEntity>>> {
        return object :
            NetworkBoundResource<PagedList<AccomDataEntity>, List<DataItemAllAccom>>(appExecutors) {
            override fun loadFromDB(): LiveData<PagedList<AccomDataEntity>> {
                val config = PagedList.Config.Builder()
                    .setEnablePlaceholders(false)
                    .setInitialLoadSizeHint(10)
                    .setPageSize(10)
                    .build()
                return LivePagedListBuilder(localDataSource.getAccom(), config).build()
            }

            override fun shouldFetch(data: PagedList<AccomDataEntity>?): Boolean =
//                data == null || data.isEmpty()
                true
            override fun createCall(): LiveData<ApiResponse<List<DataItemAllAccom>>> =
                remoteDataSource.getAccom()

            override fun saveCallResult(data: List<DataItemAllAccom>) {
                val placeList = ArrayList<AccomDataEntity>()
                for (response in data) {
                    val movie = AccomDataEntity(
                        id_car = response.id,
                        no_car = response.noCar,
                        name = response.name,
                        status = response.status,
                        rent_price = response.rentPrice,
                        passenger_capacity = response.passengerCapacity.toString(),
                        pictures = response.pictures?.first()?.picture
                    )
                    placeList.add(movie)
                }
                localDataSource.insertAccom(placeList)
            }
        }.asLiveData()
    }

    override fun getDetailAccom(id: Int): LiveData<AccomDataEntity> {
        return localDataSource.getDetailAccom(id)
    }

    // WAITING FOR POJO JSON DETAIL ACCOM API
//    override fun getDetailAccom(accomdId: Int): LiveData<Resource<AccomDataEntity>> {
//        return object : NetworkBoundResource<AccomDataEntity, Place>(appExecutors) {
//            override fun loadFromDB(): LiveData<AccomDataEntity> =
//                localDataSource.getDetailAccom(accomdId)
//
//            override fun shouldFetch(data: TourismDataEntity?): Boolean =
//                data?.title.isNullOrEmpty()
//
//            override fun createCall(): LiveData<ApiResponse<Place>> =
//                remoteDataSource.getDetailTourism(accomdId)
//
//            override fun saveCallResult(data: Place) {
//                val tourismDetail = TourismDataEntity(
//                    id = data.id,
//                    thumbnail = data.thumbnail,
//                    address = data.address,
//                    latitude = data.latitude,
//                    longtitude = data.longtitude,
//                    title = data.title,
//                    type = data.type,
//                    slug = data.slug,
//                    desc = data.desc
//                )
//                localDataSource.updatePlace(tourismDetail)
//            }
//        }.asLiveData()
//    }

    override fun setPlaceFavorite(place: TourismDataEntity, state: Boolean) {
        appExecutors.diskIO().execute { localDataSource.setPlaceFavorite(place, state) }
    }

    override fun getPlacesFavorite(): LiveData<List<TourismDataEntity>> {
        return localDataSource.getPlacesFavorite()
    }

    override fun insertNode(guide: GuideMapsEntity) {
        appExecutors.diskIO().execute { localDataSource.insertNode(guide) }
    }

    override fun getNodes(): LiveData<List<GuideMapsEntity>> {
        return localDataSource.getNodes()
    }

    override fun deleteNode() {
        appExecutors.diskIO().execute { localDataSource.deleteNode() }
    }

    override fun deleteNodes() {
        appExecutors.diskIO().execute { localDataSource.deleteNodes() }
    }

    override fun getAllRecordGuide(backpackerId : String): LiveData<Resource<RecordGuideEntity>> {
        return object : NetworkBoundResource<RecordGuideEntity, DetailGuideResponse>(appExecutors){
            override fun loadFromDB(): LiveData<RecordGuideEntity> =
                localDataSource.getAllRecordGuide(backpackerId)

            override fun shouldFetch(data: RecordGuideEntity?): Boolean =
//                data?.id == null
                    true

            override fun createCall(): LiveData<ApiResponse<DetailGuideResponse>> =
                remoteDataSource.getRecordBackpacker(backpackerId)

            override fun saveCallResult(data: DetailGuideResponse) {
                val record = RecordGuideEntity(
                    backpackerId = data.idBackpacker,
                    noHp = data.noHp.toString(),
                    vacationCount = data.jumlahPerjalanan,
                )
                val tmpListRecordVacation = ArrayList<RecordVacationListEntity>()
                data.listPerjalanan?.forEachIndexed { index, _ ->
                    val recordVacation = RecordVacationListEntity(
                        id = UUID.randomUUID().toString(),
                        idBackpacker = backpackerId,
                        tglPerjalanan = data.listPerjalanan[index]?.tglPerjalanan,
                        idTempatWisata = data.listPerjalanan[index]?.idTempatWisata?.toString(),
                        idPerjalanan = data.listPerjalanan[index]?.idPerjalanan,
                        idAkomodasi = data.listPerjalanan[index]?.idAkomodasi.toString(),
                        tglPulang = data.listPerjalanan[index]?.tglPulang
                    )
                    Log.d("listrecvac", recordVacation.toString())
                    tmpListRecordVacation.add(recordVacation)
                    Log.d("listrecvacrec", tmpListRecordVacation.toString())
                }

                Log.d("listrecvacrec2", tmpListRecordVacation.toString())
                if(localDataSource.getRecordVacation(backpackerId).value?.size != 0){
                    localDataSource.deleteRecordVacation(backpackerId)
                    localDataSource.insertRecordVacation(tmpListRecordVacation)
                    localDataSource.insertRecordGuide(record)
                } else {
                    localDataSource.insertRecordVacation(tmpListRecordVacation)
                    localDataSource.insertRecordGuide(record)
                }
            }
        }.asLiveData()
    }

    override fun getRecordVacation(backpackerId: String): LiveData<List<RecordVacationListEntity>> {
        return localDataSource.getRecordVacation(backpackerId)
    }

    override fun getDetailRecordVacation(idPerjalanan: Int): LiveData<RecordVacationListEntity> {
        return localDataSource.getDetailRecordVacation(idPerjalanan)
    }

    companion object {
        @Volatile
        private var instance: TourismRepository? = null

        fun getInstance(
            remoteData: RemoteDataSource,
            localData: LocalDataSource,
            appExecutors: AppExecutors
        ): TourismRepository =
            instance ?: synchronized(this) {
                instance ?: TourismRepository(remoteData, localData, appExecutors).apply {
                    instance = this
                }
            }
    }

}