package com.smart.smartbalibackpaker.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.core.data.source.remote.ConfigNetwork
import com.smart.smartbalibackpaker.core.data.source.remote.response.ResponseAction
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status
import com.smart.smartbalibackpaker.dashboard.PlaceViewModel
import com.smart.smartbalibackpaker.databinding.ActivityChooseAccomBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseAccomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChooseAccomBinding
    private val placeViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        ).get(PlaceViewModel::class.java)
    }
    private val registSecondFormViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        ).get(RegistSecondFormViewModel::class.java)
    }
    private var adapter = ChooseAccomAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChooseAccomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initiate Variable from RegistFirstForm and RegistSecondForm
        val email = intent.getStringExtra(EXTRA_EMAIL).toString()
        val nama = intent.getStringExtra(EXTRA_NAMA).toString()
        val noHp = intent.getStringExtra(EXTRA_NOHP).toString()
        val tglDatang = intent.getStringExtra(EXTRA_DATANG).toString()
        val tglPergi = intent.getStringExtra(EXTRA_PERGI).toString()
        val tmpDatang = intent.getStringExtra(EXTRA_TEMP_DATANG).toString()
        val choosePlace = intent.getStringExtra(PLACE_DATA).toString()
        val accomData = intent.getStringExtra(EXTRA_ID_ACCOM) ?: "22"
        val accomName = intent.getStringExtra(EXTRA_NAME_CAR_ACCOM) ?: "-"
        Log.d("ACCOMDATA", accomData)
        var sendPlaces = ""
        if (choosePlace.toString() != "PLACE_DATA" && choosePlace.toString() != "null") {

            val arrayAllPlace: List<String> = choosePlace.split(",")
            val arrayEd = ArrayList(arrayAllPlace)
            arrayEd.removeAt(0)
            sendPlaces = arrayEd.toString()
                .replace("[","")
                .replace("]","")
                .trim()
        }

        binding.textViewTitleDaftar.text = "Pilihan : $accomName" ?: "Pilih Akomodasi Anda"

        Log.d("TESTRESP", "$email,\n" +
                "                $nama,\n" +
                "                $noHp,\n" +
                "                $tglDatang,\n" +
                "                $tglPergi,\n" +
                "                $tmpDatang,\n" +
                "                $accomData,\n" +
                "                $sendPlaces")

        showProgressBarTouristPlace(true)

        getTouristPlaceData()

        showRecyclerViewTouristPlace()

        if (accomData != "-" && accomData.toString() != "null"){
            binding.btnFinalRegister.text = "Simpan dan Unggah"
        }

        binding.btnFinalRegister.setOnClickListener {
//            registSecondFormViewModel.uploadResult(
//                email,
//                nama,
//                noHp,
//                tglDatang,
//                tglPergi,
//                tmpDatang,
//                accomData,
//                choosePlace
//            ).observe(this, { user ->
//                val result = user.data
//                val message = result?.isSuccess
//                Toast.makeText(this, "Upload :"+result?.message+" and "+message, Toast.LENGTH_SHORT).show()
//            })


//            ConfigNetwork.getRetrofit().addRecordBackpacker(
//                email,
//                nama,
//                noHp,
//                tglDatang,
//                tglPergi,
//                tmpDatang,
//                accomData,
//                "",
//                choosePlace
//            ).enqueue(object : Callback<ResponseAction> {
//                override fun onResponse(
//                    call: Call<ResponseAction>,
//                    response: Response<ResponseAction>
//                ) {
//                    val response1 = response.body()
//                    Log.d("response", response1.toString())
//                    Toast.makeText(this@ChooseAccomActivity, "BERHASIL", Toast.LENGTH_SHORT).show()
//                }
//
//                override fun onFailure(call: Call<ResponseAction>, t: Throwable) {
//                    val response = t.message
//                    Toast.makeText(this@ChooseAccomActivity, "GAAA", Toast.LENGTH_SHORT).show()
//
//                }
//
//            })
            Log.d("DATAUNTUKPOST",
                "$email, $noHp, $tmpDatang, $tglDatang, $tglPergi, $sendPlaces, 3, $accomData"
            )
            ConfigNetwork.getRetrofit().addRecordBackpacker(
                email,
//                nama,
                noHp,
                tmpDatang,
                tglDatang,
                tglPergi,
                sendPlaces,
                "3",
                accomData
                ).enqueue(object : Callback<ResponseAction>{
                override fun onResponse(
                    call: Call<ResponseAction>,
                    response: Response<ResponseAction>
                ) {
                    val response1 = response.body()
                    Log.d("POSTRESPONSE", response1.toString())
                    Toast.makeText(this@ChooseAccomActivity, "BERHASIL", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(call: Call<ResponseAction>, t: Throwable) {
                    val response = t.message
                    Log.d("POSTRESPONSE", response.toString())

                    Toast.makeText(this@ChooseAccomActivity, response, Toast.LENGTH_SHORT).show()

                }

            })
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(MainActivity.EXTRA_CHECK, 1)
            startActivity(intent)
            finishAffinity()
        }
    }

    private fun showRecyclerViewTouristPlace() {
        binding.apply {
            rvTourist.layoutManager = GridLayoutManager(this@ChooseAccomActivity, 1)
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

    private fun getTouristPlaceData() {
        placeViewModel.getAccom().observe(this, {
            Log.d("INIACCOM1", it.data.toString())

            if (it != null) {
                when (it.status) {
                    Status.LOADING -> showProgressBarTouristPlace(true)
                    Status.SUCCESS -> {
                        adapter.submitList(it.data)
                        Log.d("INIACCOM2", it.data.toString())
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

        const val PLACE_DATA_EXIST = "EXIST_DATA"

        const val NEXT_NODE_LAT = "-8.7383124"
        const val NEXT_NODE_LONG = "115.2101971"

        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"

        const val EXTRA_ID_ACCOM = "extra_id_accom"
        const val EXTRA_NAME_ACCOM = "extra_name_accom"
        const val EXTRA_NAME_CAR_ACCOM = "extra_name_accom"

        const val PLACE_DATA = "PLACE_DATA"

        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_NAMA = "EXTRA_NAMA"
        const val EXTRA_NOHP = "EXTRA_NOHP"
        const val EXTRA_DATANG = "EXTRA_DATANG"
        const val EXTRA_PERGI = "EXTRA_PERGI"
        const val EXTRA_TEMP_DATANG = "EXTRA_TEMP_DATANG"

    }
}
