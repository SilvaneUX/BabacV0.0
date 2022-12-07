package com.smart.smartbalibackpaker.registration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import com.google.android.material.datepicker.MaterialDatePicker
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.databinding.ActivityRegistFirstFormBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

import android.widget.AdapterView.OnItemClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.smart.smartbalibackpaker.core.utils.Formatter
import java.text.NumberFormat


class RegistFirstFormActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistFirstFormBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseDatabase
    private var user: FirebaseUser? = null
    private lateinit var dbReference: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistFirstFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance()
        dbReference = db.getReference("users")
        storage = FirebaseStorage.getInstance()
        storageRef = storage.getReference()
        userId = auth.currentUser?.uid
        user = auth.currentUser

        binding.btnRegistDatepicker.setOnClickListener {
            showDataRangePicker()
        }

        val items = listOf("Pelabuhan Gilimanuk", "Pelabuhan Benoa", "Bandara Internasional Ngurah Rai")
        val adapter = ArrayAdapter(this, R.layout.list_item, items)
        (binding.listArrivalPlace as? AutoCompleteTextView)?.setAdapter(adapter)


        var harga = 0.0

        var selectedArrivalPlace = ""
        binding.listArrivalPlace.onItemClickListener =
            OnItemClickListener { parent, view, position, rowId ->
                val selection = parent.getItemAtPosition(position) as String
                selectedArrivalPlace = selection
            }

        binding.apply {
            btnNextRegister.setOnClickListener {
                when {
                    etRegistFillDate1.text.toString().trim().isEmpty() -> {
                        etRegistFillDate1.error = "Tanggal kedatangan harus diisi"
                        etRegistFillDate2.error = "Tanggal kepulangan harus diisi"
                    }
                    edtRegistName.text.toString().trim().isEmpty() -> {
                        edtRegistName.error = "Nama backpacker harus diisi"
                    }
                    edtRegistPhone.text.toString().trim().isEmpty() -> {
                        edtRegistPhone.error = "No. Telp backpacker harus diisi"
                    }
                    listArrivalPlace.text.toString().trim().isEmpty() -> {
                        listArrivalPlace.error = "Tempat kedatangan harus dipilih"
                    } else -> {
                    val intent = Intent(this@RegistFirstFormActivity, RegistSecondFormActivity::class.java)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_EMAIL, user?.uid.toString())
                    intent.putExtra(RegistSecondFormActivity.EXTRA_NAMA, edtRegistName.text.toString().trim().toString() )
                    intent.putExtra(RegistSecondFormActivity.EXTRA_NOHP, edtRegistPhone.text.toString().trim().toString())
                    intent.putExtra(RegistSecondFormActivity.EXTRA_DATANG, etRegistFillDate1.text.toString().trim().toString())
                    intent.putExtra(RegistSecondFormActivity.EXTRA_PERGI, etRegistFillDate2.text.toString().trim().toString())
                    intent.putExtra(RegistSecondFormActivity.EXTRA_TEMP_DATANG, selectedArrivalPlace)
                    intent.putExtra(RegistSecondFormActivity.EXTRA_BUDGET, edtRegistBudget.text.toString())
                    startActivity(intent)
                }

                }
            }
            edtRegistBudget.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    val text = edtRegistBudget.text.toString()
                    harga = text.toDouble()
                    etBudgetAlloc.text = Formatter.rupiahFormatter(harga)
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        }

        binding.icBack.setOnClickListener{
            finish()
        }
    }


    private fun showDataRangePicker() {

        val dateRangePicker =
            MaterialDatePicker
                .Builder.dateRangePicker()
                .setTitleText("Select Date")
                .build()

        dateRangePicker.show(
            supportFragmentManager,
            "date_range_picker"
        )

        dateRangePicker.addOnPositiveButtonClickListener { dateSelected ->

            val startDate = dateSelected.first
            val endDate = dateSelected.second

            val vacationDuration = TimeUnit.DAYS.convert(endDate-startDate, TimeUnit.MILLISECONDS)

            if (startDate != null && endDate != null) {
                binding.etRegistFillDate1.setText("${convertLongToTime(startDate)}")
                binding.etRegistFillDate2.setText("${convertLongToTime(endDate)}")
                binding.btnRegistDatepicker.text = "$vacationDuration Day(s)"
            }


        }

    }


    private fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault())
        return format.format(date)
    }

    companion object{

    }
}