package com.smart.smartbalibackpaker.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.rd.utils.CoordinatesUtils.getCoordinate
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.data.source.remote.ApiResponse
import com.smart.smartbalibackpaker.core.data.source.remote.ConfigNetwork
import com.smart.smartbalibackpaker.core.data.source.remote.response.DataItem
import com.smart.smartbalibackpaker.core.data.source.remote.response.TourismResponse
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status
import com.smart.smartbalibackpaker.dashboard.PlaceViewModel
import com.smart.smartbalibackpaker.databinding.ActivityChoosePlaceBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChoosePlaceActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var binding: ActivityChoosePlaceBinding
    private val placeViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        ).get(PlaceViewModel::class.java)
    }
    private var adapter = ChoosePlaceAdapter()

    private lateinit var mMap : GoogleMap

    val coordinateArray= ArrayList<LatLng>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoosePlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var nodeLat = intent.getStringExtra(NEXT_NODE_LAT) ?: NEXT_NODE_LAT
        var nodeLong = intent.getStringExtra(NEXT_NODE_LONG) ?: NEXT_NODE_LONG

        val email = intent.getStringExtra(EXTRA_EMAIL).toString()
        val nama = intent.getStringExtra(EXTRA_NAMA).toString()
        val noHp = intent.getStringExtra(EXTRA_NOHP).toString()
        val tglDatang = intent.getStringExtra(EXTRA_DATANG).toString()
        val tglPergi = intent.getStringExtra(EXTRA_PERGI).toString()
        val tmpDatang = intent.getStringExtra(EXTRA_TEMP_DATANG).toString()
        val budget = intent.getStringExtra(EXTRA_BUDGET)?.toInt() ?: 0

        val map = supportFragmentManager
            .findFragmentById(R.id.fg_maps) as SupportMapFragment
        map.getMapAsync(this)

//        coordinateArray = ArrayList()

        val items = listOf("Rekomendasi", "Termurah", "Budget Bundling")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding.filterPlace as? AutoCompleteTextView)?.setAdapter(adapter)

        showProgressBarTouristPlace(true)

        getTouristPlaceData(nodeLat, nodeLong, "slug", getCoordinate = object : GetCoordinate{
            override fun getCoordinate(lat: Double, long: Double) {
                val latD = LatLng(lat, long)
//                if (coordinateArray.isEmpty() || coordinateArray.toString() == "[]"){
                    coordinateArray.add(latD)
//                }
                Log.d("ARRAYLAT", coordinateArray.toString())

            }

        }, budget)

        showRecyclerViewTouristPlace()

        var selectedArrivalPlace = "slug"
        binding.filterPlace.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, rowId ->
                val selection = parent.getItemAtPosition(position) as String
                selectedArrivalPlace = selection
                when (selectedArrivalPlace) {
                    "Termurah" -> {
                        binding.btnFinalAdd.visibility = View.GONE

                        showProgressBarTouristPlace(true)

                        getTouristPlaceData(nodeLat, nodeLong, "price", getCoordinate = object : GetCoordinate{
                            override fun getCoordinate(lat: Double, long: Double) {
                                coordinateArray.add(LatLng(lat, long))
                            }
                        }, budget)

                        showRecyclerViewTouristPlace()
                    }
                    "Budget Bundling" -> {
                        binding.btnFinalAdd.visibility = View.VISIBLE

                        showProgressBarTouristPlace(true)

                        getTouristPlaceData(nodeLat, nodeLong, "priceTarget", getCoordinate = object : GetCoordinate{
                            override fun getCoordinate(lat: Double, long: Double) {
                                coordinateArray.add(LatLng(lat, long))
                            }
                        }, budget)

                        showRecyclerViewTouristPlace()
                    }
                    else -> {
                        binding.btnFinalAdd.visibility = View.GONE

                        showProgressBarTouristPlace(true)

                        getTouristPlaceData(nodeLat, nodeLong, "slug", getCoordinate = object : GetCoordinate{
                            override fun getCoordinate(lat: Double, long: Double) {
                                val latD = LatLng(lat, long)
            //                if (coordinateArray.isEmpty() || coordinateArray.toString() == "[]"){
                                coordinateArray.add(latD)
            //                }
                                Log.d("ARRAYLAT", coordinateArray.toString())

                            }

                        }, budget)
                        showRecyclerViewTouristPlace()
                    }
                }

            }

        val arrivalPlace = intent.getStringExtra(ARRIVAL_PLACE)
        Toast.makeText(this, arrivalPlace, Toast.LENGTH_SHORT).show()

        binding.textViewTitleArrivalPlace.text = arrivalPlace

//        getTouristPlaceData(nodeLat, nodeLong)

    }

    private fun showRecyclerViewTouristPlace() {
        binding.apply {
            rvTourist.layoutManager = GridLayoutManager(this@ChoosePlaceActivity, 1)
            rvTourist.setHasFixedSize(true)
            rvTourist.isNestedScrollingEnabled = true
            rvTourist.adapter = adapter
        }
    }

    private fun showProgressBarTouristPlace(status: Boolean) {
        binding.apply {
            if (status) {
                progressbarTourist.visibility = View.VISIBLE
                rvTourist.visibility = View.GONE
            } else {
                progressbarTourist.visibility = View.GONE
                rvTourist.visibility = View.VISIBLE
            }
        }
    }

    private fun getTouristPlaceData(nodeLat: String, nodeLong: String, sort: String, getCoordinate: GetCoordinate, budget: Int) {
        ConfigNetwork.getRetrofit().getAllTourism("wisata").enqueue(object :
            Callback<TourismResponse> {
            override fun onResponse(call: Call<TourismResponse>, response: Response<TourismResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()?.data
                    if (body != null) {
//                        for (data in body)
//
//                        getCoordinate.getCoordinate(data?.latitude?.toDouble()!!, data.longtitude?.toDouble()!!)

                    }
                }
            }

            override fun onFailure(call: Call<TourismResponse>, t: Throwable) {
                Log.d("Response", "On Failure")
            }
        })
        placeViewModel.getChoosePlaces("tour", nodeLat, nodeLong, sort, budget).observe(this, {
//            Log.d("INIDATA1", it.data.toString())
            val email = intent.getStringExtra(EXTRA_EMAIL).toString()
            val nama = intent.getStringExtra(EXTRA_NAMA).toString()
            val noHp = intent.getStringExtra(EXTRA_NOHP).toString()
            val tglDatang = intent.getStringExtra(EXTRA_DATANG).toString()
            val tglPergi = intent.getStringExtra(EXTRA_PERGI).toString()
            val tmpDatang = intent.getStringExtra(EXTRA_TEMP_DATANG).toString()
            val budgetLeft = intent.getStringExtra(EXTRA_BUDGET).toString()

            if (it != null) {
                when (it.status) {
                    Status.LOADING -> showProgressBarTouristPlace(true)
                    Status.SUCCESS -> {
                        adapter.submitList(it.data)
                        if (sort == "priceTarget"){
                            var dataName = ""
                            var dataId = ""
                            var dataBudget :Int = 0
                            val arrayId: ArrayList<String> = ArrayList()
                            val arrayName: ArrayList<String> = ArrayList()
                            val arrayBudget: ArrayList<Int> = ArrayList()
                            for (value in it.data!!){
                                arrayId.add(value.id.toString())
                                arrayName.add(value.title.toString())
                                value.price?.let { it1 -> arrayBudget.add(it1) }
                            }
                            dataName = arrayName.joinToString(
                                separator = ",",
                            )
                            dataId = arrayId.joinToString(
                                separator = ",",
                            )
                            val budgetNow = arrayBudget.sum()
                            dataBudget = budgetLeft.toInt() - budgetNow
                            Log.d("SISAUANG", dataBudget.toString())
                            val intent = Intent(this@ChoosePlaceActivity, RegistSecondFormActivity::class.java )
                            intent.putExtra(RegistSecondFormActivity.EXTRA_EMAIL, email)
                            intent.putExtra(RegistSecondFormActivity.EXTRA_NAMA, nama )
                            intent.putExtra(RegistSecondFormActivity.EXTRA_NOHP, noHp)
                            intent.putExtra(RegistSecondFormActivity.EXTRA_DATANG, tglDatang)
                            intent.putExtra(RegistSecondFormActivity.EXTRA_PERGI, tglPergi)
                            intent.putExtra(RegistSecondFormActivity.EXTRA_TEMP_DATANG, tmpDatang)
                            intent.putExtra(RegistSecondFormActivity.PLACE_NAME, "$tmpDatang, $dataName")
                            intent.putExtra(RegistSecondFormActivity.PLACE_DATA, "$tmpDatang, $dataId")
                            intent.putExtra(RegistSecondFormActivity.EXTRA_BUDGET, dataBudget.toString())
                            startActivity(intent)
                        }
                        else{
                            for (data in it.data!!){
                                getCoordinate.getCoordinate(data?.latitude?.toDouble()!!, data.longtitude?.toDouble()!!)
                                mMap.addMarker(MarkerOptions().position(LatLng(
                                    data.latitude?.toDouble()!!,
                                    data.longtitude?.toDouble()!!
                                )).title(data.title))
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(intent.getStringExtra(NEXT_NODE_LAT)?.toDouble()!!,intent.getStringExtra(NEXT_NODE_LONG)?.toDouble()!!), 13.0f))

//                            Log.d("INILAT1", data.latitude.toString())
//                            coordinateArray?.add(LatLng(-8.7467172, 115.1645983))
//
//                            coordinateArray!!.add(LatLng(
//                                    data.latitude!!.toDouble(),
//                                    data.longtitude!!.toDouble()
//                            ))
                            }
                        }
                        showProgressBarTouristPlace(false)
                    }
                    Status.ERROR -> {
                        showProgressBarTouristPlace(false)
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    companion object{
        const val ARRIVAL_PLACE = "ARRIVAL_PLACE"
        const val NEXT_ARRIVAL_PLACE = "ARRIVAL_PLACE"

        const val ACCOM_NAME = "ACCOM_NAME"

        const val PLACE_DATA_EXIST = "EXIST_DATA"
        const val PLACE_NAME_EXIST = "EXIST_NAME"

        const val NEXT_NODE_LAT = "-8.7383124"
        const val NEXT_NODE_LONG = "115.2101971"

        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"

        const val EXTRA_ID_ACCOM = "extra_id_accom"
        const val EXTRA_NAME_ACCOM = "extra_name_accom"

        const val PLACE_DATA = "PLACE_DATA"
        const val DELETED_PLACE_DATA = "DELETED_PLACE_DATA"

        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_NAMA = "EXTRA_NAMA"
        const val EXTRA_NOHP = "EXTRA_NOHP"
        const val EXTRA_DATANG = "EXTRA_DATANG"
        const val EXTRA_PERGI = "EXTRA_PERGI"
        const val EXTRA_TEMP_DATANG = "EXTRA_TEMP_DATANG"
        const val EXTRA_BUDGET = "0"

    }

    override fun onMapReady(googleMap: GoogleMap) {

        val email = intent.getStringExtra(EXTRA_EMAIL).toString()
        val nama = intent.getStringExtra(EXTRA_NAMA).toString()
        val noHp = intent.getStringExtra(EXTRA_NOHP).toString()
        val tglDatang = intent.getStringExtra(EXTRA_DATANG).toString()
        val tglPergi = intent.getStringExtra(EXTRA_PERGI).toString()
        val tmpDatang = intent.getStringExtra(EXTRA_TEMP_DATANG).toString()
        val budget = intent.getStringExtra(EXTRA_BUDGET)?.toInt() ?: 0

        mMap = googleMap

        if (!intent.getStringExtra(NEXT_NODE_LAT).isNullOrEmpty()) {

            coordinateArray.add(
                LatLng(
                    intent.getStringExtra(NEXT_NODE_LAT)?.toDouble()!!,
                    intent.getStringExtra(NEXT_NODE_LONG)?.toDouble()!!
                )
            )
        }


        Log.d("ARRAYLATNIH", coordinateArray.toString())

        for (i in coordinateArray.indices){
            val snippet = intent.getStringExtra(ARRIVAL_PLACE)
            mMap.addMarker(MarkerOptions()
                .position(coordinateArray[i])
                .title("Previous Place")
                .snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinateArray[i], 13.0f))
        }

        mMap.setOnMarkerClickListener { marker ->
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
                Toast.makeText(this@ChoosePlaceActivity, "HALOO CLICKER", Toast.LENGTH_SHORT).show()
//                val desiredString = intent.getStringExtra(ARRIVAL_PLACE)
//                val desiredPlace = intent.getStringExtra(PLACE_DATA_EXIST)  ?: desiredString
//
//                val intent = Intent(this, RegistSecondFormActivity::class.java)
//                if (desiredPlace != "PLACE_DATA" && desiredPlace != "null") {
//                    intent.putExtra(RegistSecondFormActivity.PLACE_DATA, "$desiredPlace,${marker.title}")
//                    intent.putExtra(RegistSecondFormActivity.DELETED_PLACE_DATA, 0)
//                    intent.apply {
//                        putExtra(RegistSecondFormActivity.EXTRA_ID, 0)
//                        putExtra(RegistSecondFormActivity.EXTRA_NAME, marker.title)
////                    putExtra(RegistSecondFormActivity.PLACE_DATA, "$desiredPlace,${place.title},")
//                    }
//                }
//                startActivity(intent)
            } else {
                marker.showInfoWindow()
                Toast.makeText(this@ChoosePlaceActivity, "HALOO CLICKER 2", Toast.LENGTH_SHORT).show()
                val desiredString = intent.getStringExtra(ARRIVAL_PLACE)
                val desiredPlace = intent.getStringExtra(PLACE_DATA_EXIST)  ?: desiredString
//                val desiredPlaceName = intent.getStringExtra(PLACE_NAME_EXIST)  ?: desiredString

                val intent = Intent(this, RegistSecondFormActivity::class.java)
                if (desiredPlace != "PLACE_DATA" && desiredPlace != "null") {
                    intent.putExtra(RegistSecondFormActivity.PLACE_DATA, "$desiredPlace,${marker.id}")
//                    intent.putExtra(RegistSecondFormActivity.PLACE_NAME, "$desiredPlaceName,${marker.title}")
                    intent.putExtra(RegistSecondFormActivity.DELETED_PLACE_DATA, 0)
                    intent.apply {
                        putExtra(RegistSecondFormActivity.EXTRA_ID, 0)
                        putExtra(RegistSecondFormActivity.EXTRA_NAME, marker.title)
//                    putExtra(RegistSecondFormActivity.PLACE_DATA, "$desiredPlace,${place.title},")
                    }

                    intent.putExtra(RegistSecondFormActivity.EXTRA_EMAIL, email)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_NAMA, nama )
                    intent.putExtra(RegistSecondFormActivity.EXTRA_NOHP, noHp)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_DATANG, tglDatang)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_PERGI, tglPergi)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_TEMP_DATANG, tmpDatang)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_BUDGET, budget.toString())
                }
                startActivity(intent)
            }
            true
        }
    }

}