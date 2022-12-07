package com.smart.smartbalibackpaker.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.smart.smartbalibackpaker.core.data.source.local.entity.ListRegistPlaceEntity
import com.smart.smartbalibackpaker.core.utils.Formatter
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.databinding.ActivityRegistSecondFormBinding
import com.smart.smartbalibackpaker.core.vo.Status

class RegistSecondFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistSecondFormBinding
    var nextLat = ""
    var nextLong = ""
    var nextArrivalNode = ""
    private var arrayFixed = ArrayList<String>()

    private val registSecondFormViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.getInstance(this)
        ).get(RegistSecondFormViewModel::class.java)
    }

    private val list = ArrayList<ListRegistPlaceEntity>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistSecondFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initiate Variable from RegistFirstForm
        val email = intent.getStringExtra(EXTRA_EMAIL).toString()
        val nama = intent.getStringExtra(EXTRA_NAMA).toString()
        val noHp = intent.getStringExtra(EXTRA_NOHP).toString()
        val tglDatang = intent.getStringExtra(EXTRA_DATANG).toString()
        val tglPergi = intent.getStringExtra(EXTRA_PERGI).toString()
        val tmpDatang = intent.getStringExtra(EXTRA_TEMP_DATANG).toString()
        var budget = intent.getStringExtra(EXTRA_BUDGET)?.toInt() ?: 0
        var budgetLeft = intent.getStringExtra(EXTRA_BUDGET_LEFT)?.toInt() ?:0
        val akomodasi = binding.etRegistFillAccom.text.toString()
        val choosePlace = binding.etRegistFillPlace.text.toString().trim().toString()

        binding.etBudgetLeft.text = Formatter.rupiahFormatter(budget.toDouble())

        Log.d("TESTKIRIMEMAIL", email)

//        if(DELETED_PLACE_DATA.isNotEmpty()){
//            val deletedData = intent.getStringExtra(DELETED_PLACE_DATA)
//            if (deletedData != null) {
//                registSecondFormViewModel.deleteTourism(deletedData.toInt())
//            }
//        }

        // Retrieve Data from ChoosePlaceActivity
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val name = intent.getStringExtra(PLACE_DATA)
        val nameList = intent.getStringExtra(PLACE_NAME)
        var sendPlaces = ""
//        if (name.toString() != "PLACE_DATA" && name.toString() != "null") {
//
//            val arrayAllPlace: List<String> = name!!.split(",")
//            val arrayEd = ArrayList(arrayAllPlace)
//        }
        val accomData = intent.getStringExtra(EXTRA_NAME_ACCOM)

        if (accomData.toString() != "extra_name_accom" && accomData.toString() != "null") {
            binding.etRegistFillAccom.setText(accomData)
//            getDetails(id)
        }

        // Cond. Check (First OpenPage == No Data from Choosen Place in ChoosePlace Activity)
        if (name.toString() != "PLACE_DATA" && name.toString() != "null") {
            binding.etRegistFillPlace.setText(name)
//            binding.etBudgetLeft.setText(budget)
            Log.d("PLACENAME", name.toString())

            Log.d("BUDGETSISA", budget.toString())
            // Data Splitting to Array (Retrieve last index for Next Lat and Long)
            val yourArrayId: List<String> = name!!.split(",")
            val yourArrayName: List<String> = nameList!!.split(",")
            var arrayListPlaces = ArrayList<String>()

//            for (data in yourArrayId){
////                arrayFixed.add(getNameDetails(data.toIntOrNull() ?: 0))
//                Log.d("ARRAYFIXED", data.toString())
//                registSecondFormViewModel.setSelectedPlace(id)
//                registSecondFormViewModel.detailPlace.observe(this, {
//                    if (it != null) {
//                        when (it.status) {
//                            Status.LOADING -> {
//                                Toast.makeText(this, "Adding..", Toast.LENGTH_SHORT).show()
//                            }
//                            Status.SUCCESS -> {
//                                Toast.makeText(this, it.data?.title.toString(), Toast.LENGTH_SHORT).show()
//                                arrayFixed.add(it.data?.title.toString())
////                                list.addAll(getListPlaces(arrayListPlaces))
//
//
//                            }
//                            Status.ERROR -> {
//                                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                })
//            }
//            val yourArrayName: List<String> = nameList!!.split(",")
            nextArrivalNode = yourArrayId.last()

            arrayListPlaces = ArrayList(yourArrayName)
            list.addAll(getListPlaces(arrayListPlaces))
            showRecyclerViewChoosenPlace()

            // Initiate next lat and long
            getDetails(id)

        }else {
            // Define first lat and long
            when (tmpDatang) {
                "Pelabuhan Gilimanuk" -> {
                    nextLat = "-8.160951"
                    nextLong = "114.4347003"
                }
                "Pelabuhan Benoa" -> {
                    nextLat = "-8.7383124"
                    nextLong = "115.2101971"
                }
                "Bandara Internasional Ngurah Rai" -> {
                    nextLat = "-8.7467172"
                    nextLong = "115.1645983"
                }
            }}

        binding.btnRegistAccomodation.setOnClickListener {
            val intent = Intent(this, ChooseAccomActivity::class.java)
//            intent.putExtra(ChooseAccomActivity.NEXT_NODE_LAT, nextLat)
//            intent.putExtra(ChooseAccomActivity.NEXT_NODE_LONG, nextLong)
//            intent.putExtra(ChooseAccomActivity.ARRIVAL_PLACE, tmpDatang)
//            intent.putExtra(ChooseAccomActivity.ACCOM_NAME, accomData ?: "-")
//            if (name.toString() != "PLACE_DATA" && name.toString() != "null") {
//                intent.putExtra(ChooseAccomActivity.PLACE_DATA_EXIST, name)
//                intent.putExtra(ChooseAccomActivity.NEXT_NODE_LAT, nextLat)
//                intent.putExtra(ChooseAccomActivity.NEXT_NODE_LONG, nextLong)
//                intent.putExtra(ChooseAccomActivity.ARRIVAL_PLACE, nextArrivalNode)
//            }
            startActivity(intent)
        }

        binding.icBack.setOnClickListener{
            finish()
        }

        binding.btnRegistPlace.setOnClickListener {
            val intent = Intent(this, ChoosePlaceActivity::class.java)
            intent.putExtra(ChoosePlaceActivity.EXTRA_EMAIL, email)
            intent.putExtra(ChoosePlaceActivity.EXTRA_NAMA, nama )
            intent.putExtra(ChoosePlaceActivity.EXTRA_NOHP, noHp)
            intent.putExtra(ChoosePlaceActivity.EXTRA_DATANG, tglDatang)
            intent.putExtra(ChoosePlaceActivity.EXTRA_PERGI, tglPergi)
            intent.putExtra(ChoosePlaceActivity.EXTRA_TEMP_DATANG, tmpDatang)
            intent.putExtra(ChoosePlaceActivity.EXTRA_BUDGET, budget.toString())

            intent.putExtra(ChoosePlaceActivity.NEXT_NODE_LAT, nextLat)
            intent.putExtra(ChoosePlaceActivity.NEXT_NODE_LONG, nextLong)
            intent.putExtra(ChoosePlaceActivity.ARRIVAL_PLACE, tmpDatang)
            intent.putExtra(ChoosePlaceActivity.ACCOM_NAME, accomData ?: "-")
            if (name.toString() != "PLACE_DATA" && name.toString() != "null") {

                intent.putExtra(ChoosePlaceActivity.EXTRA_EMAIL, email)
                intent.putExtra(ChoosePlaceActivity.EXTRA_NAMA, nama )
                intent.putExtra(ChoosePlaceActivity.EXTRA_NOHP, noHp)
                intent.putExtra(ChoosePlaceActivity.EXTRA_DATANG, tglDatang)
                intent.putExtra(ChoosePlaceActivity.EXTRA_PERGI, tglPergi)
                intent.putExtra(ChoosePlaceActivity.EXTRA_TEMP_DATANG, tmpDatang)
                intent.putExtra(ChoosePlaceActivity.EXTRA_BUDGET, budget.toString())

                intent.putExtra(ChoosePlaceActivity.PLACE_DATA_EXIST, name)
                intent.putExtra(ChoosePlaceActivity.PLACE_NAME_EXIST, nameList)
                intent.putExtra(ChoosePlaceActivity.NEXT_NODE_LAT, nextLat)
                intent.putExtra(ChoosePlaceActivity.NEXT_NODE_LONG, nextLong)
                intent.putExtra(ChoosePlaceActivity.ARRIVAL_PLACE, nextArrivalNode)
            }
            startActivity(intent)

// JUST TRIED
//            val i = Intent(it.context, ChoosePlaceActivity::class.java)
//            val emailIntent = (it.context as Activity).intent
//            val email = emailIntent.getStringExtra("email")
//            i.putExtra("email", email)
//
//            it.context.startActivity(i)

// INTENT CHOOSE PLACE FOR FRAGMENT
//            val fragment = ChoosePlaceFragment()
//            val arguments = Bundle()
//            if (name != "PLACE_DATA") {
//                Log.d("INI NAMA TEMPATE", name.toString())
//                arguments.putString(ChooseTouristPlaceFragment.PLACE_DATA_EXIST, name)
//            }
//            fragment.arguments = arguments
//            val ft = supportFragmentManager.beginTransaction()
//            ft.replace(R.id.activity_regist2nd_form, fragment)
//            ft.commit()

//            val transaction = supportFragmentManager.beginTransaction()
//            transaction.replace(R.id.activity_regist2nd_form, ChoosePlaceFragment())
//            transaction.disallowAddToBackStack()
//            transaction.commit()
        }

        binding.btnFinalRegister.setOnClickListener {
//            registSecondFormViewModel.uploadResult(
//                email,
//                nama,
//                noHp,
//                tglDatang,
//                tglPergi,
//                tmpDatang,
//                akomodasi,
//                choosePlace
//            ).observe(this, { user ->
//                val result = user.data
//                val message = result?.isSuccess
//                Toast.makeText(this, "Upload :"+result?.message+" and "+message, Toast.LENGTH_SHORT).show()
//            })
            val intent = Intent(this@RegistSecondFormActivity, ChooseAccomActivity::class.java)
            intent.putExtra(ChooseAccomActivity.EXTRA_EMAIL, email)
            intent.putExtra(ChooseAccomActivity.EXTRA_NAMA, nama)
            intent.putExtra(ChooseAccomActivity.EXTRA_NOHP, noHp)
            intent.putExtra(ChooseAccomActivity.EXTRA_DATANG, tglDatang)
            intent.putExtra(ChooseAccomActivity.EXTRA_PERGI, tglPergi)
            intent.putExtra(ChooseAccomActivity.EXTRA_TEMP_DATANG, tmpDatang)
            intent.putExtra(ChooseAccomActivity.PLACE_DATA, name)
            startActivity(intent)
        }
    }

    private fun getListPlaces(listPlaces: ArrayList<String>): ArrayList<ListRegistPlaceEntity> {
        val listPlaceFix = ArrayList<ListRegistPlaceEntity>()
        for (position in listPlaces.indices) {
            val place = ListRegistPlaceEntity(
                listPlaces[position]
            )
            listPlaceFix.add(place)
        }
        return listPlaceFix
    }

    private fun showRecyclerViewChoosenPlace() {
        binding.apply {
            rvListRegist.layoutManager = GridLayoutManager(this@RegistSecondFormActivity, 1)
            rvListRegist.setHasFixedSize(true)
            rvListRegist.isNestedScrollingEnabled = true
            val adapter = ListRegistPlaceAdapter(list)
            rvListRegist.adapter = adapter
        }
    }

    private fun getDetails(id: Int) {
        registSecondFormViewModel.setSelectedPlace(id)
        registSecondFormViewModel.detailPlace.observe(this, {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        Toast.makeText(this, "Adding..", Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        nextLat = it.data?.latitude.toString()
                        nextLong = it.data?.longtitude.toString()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getNameDetails(id: Int): String {
        var name = ""
        registSecondFormViewModel.setSelectedPlace(id)
        registSecondFormViewModel.detailPlace.observe(this, {
            if (it != null) {
                when (it.status) {
                    Status.LOADING -> {
                        name = it.data?.title.toString()
                        Toast.makeText(this, "Adding..", Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        Toast.makeText(this, it.data?.title.toString(), Toast.LENGTH_SHORT).show()
                        name = it.data?.title.toString()
                    }
                    Status.ERROR -> {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
        return name
    }

    companion object {
        const val EXTRA_ID = "extra_id"
        const val EXTRA_NAME = "extra_name"

        const val EXTRA_ID_ACCOM = "extra_id_accom"
        const val EXTRA_NAME_ACCOM = "extra_name_accom"

        const val PLACE_DATA = "PLACE_DATA"
        const val PLACE_NAME = "PLACE_NAME"
        const val DELETED_PLACE_DATA = "DELETED_PLACE_DATA"

        const val EXTRA_EMAIL = "EXTRA_EMAIL"
        const val EXTRA_NAMA = "EXTRA_NAMA"
        const val EXTRA_NOHP = "EXTRA_NOHP"
        const val EXTRA_DATANG = "EXTRA_DATANG"
        const val EXTRA_PERGI = "EXTRA_PERGI"
        const val EXTRA_TEMP_DATANG = "EXTRA_TEMP_DATANG"
        const val EXTRA_BUDGET = "0"
        const val EXTRA_BUDGET_LEFT = "0"
    }
}