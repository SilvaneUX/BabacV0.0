package com.smart.smartbalibackpaker.guide

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordGuideEntity
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status
import com.smart.smartbalibackpaker.databinding.FragmentGuideTourBinding
import java.text.SimpleDateFormat
import java.util.*


class GuideTourFragment : Fragment() {

    private var binding: FragmentGuideTourBinding? = null
    private val recordGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity())
    ).get(RecordGuideViewModel::class.java)
    }
    private lateinit var outsideAdapter : RecordGuideAdapter
    private var user: FirebaseUser? = null
    private var myUid: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGuideTourBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth()
        populateData()
        setDate()

//        binding?.btnDetailGuide?.setOnClickListener {
//            startActivity(Intent(context, DetailGuideActivity::class.java))
//        }
    }

    private fun setDate() {
        val timeInMillis = System.currentTimeMillis()
        val call = Calendar.getInstance()
        call.timeInMillis = timeInMillis

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = dateFormat.format(call.time)

        binding?.tvGuideDate?.text = date
    }

    private fun firebaseAuth() {
        user = FirebaseAuth.getInstance().currentUser
        myUid = user?.uid
    }

    private fun populateData() {
        recordGuideViewModel.getAllRecordGuide(myUid ?: "").observe(viewLifecycleOwner){
            if(it != null){
                when(it.status){
                    Status.LOADING -> {
                        showProgressBarTouristPlace(true)
                    }
                    Status.SUCCESS -> {
                        val record = it.data
                        showProgressBarTouristPlace(false)
                        Log.d("idguide", myUid ?: "")

                        recordGuideViewModel.getRecordVacation(myUid ?: "").observe(viewLifecycleOwner){ listVac ->
                            Log.d("hahaha1", listVac.toString())
//                        showProgressBarTouristPlace(false)
//                            recordGuideViewModel.getAccom().observe(viewLifecycleOwner){ accom ->
//                                if (accom != null){
//                                    when(accom.status){
//                                        Status.LOADING -> {}
//                                        Status.SUCCESS -> {
////                                            showProgressBarTouristPlace(false)
//                                            listVac.forEachIndexed{index, _ ->
//                                                accom.data?.forEachIndexed { carIndex, _ ->
//                                                    val idAccom = listVac[index].idAkomodasi?.split(".")?.toList()
//                                                    if((idAccom?.get(0)?.toInt()
//                                                            ?: 0) == accom.data[carIndex]?.id_car
//                                                    ){
//                                                        recordGuideViewModel.getDetailAccom(accom.data[carIndex]?.id_car ?: 0)
//                                                            .observe(viewLifecycleOwner){car ->
////                                                                showProgressBarTouristPlace(false)
//                                                                outsideAdapter = RecordGuideAdapter(record ?: RecordGuideEntity() , listVac, car)
//                                                                showRecyclerView(outsideAdapter)
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        }
//                                        Status.ERROR -> {}
//                                    }
//                                }
//                            }
                            showProgressBarTouristPlace(false)
                            outsideAdapter = RecordGuideAdapter(record ?: RecordGuideEntity() , listVac)
                            showRecyclerView(outsideAdapter)
                        }
                    }
                    Status.ERROR -> {
//                        showProgressBarTouristPlace(false)
                    }
                }
            }
        }
    }

    private fun showProgressBarTouristPlace(status: Boolean) {
        binding?.apply {
            if (status) {
                pbGuideTour.visibility = View.VISIBLE
                rvVacationList.visibility = View.GONE
            } else {
                pbGuideTour.visibility = View.GONE
                rvVacationList.visibility = View.VISIBLE
            }
        }
    }

    private fun showRecyclerView(outsideAdapter: RecordGuideAdapter) {
        binding?.rvVacationList?.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = outsideAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}