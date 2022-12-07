package com.smart.smartbalibackpaker.guide

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.dashboard.DetailPlaceViewModel
import com.smart.smartbalibackpaker.databinding.ActivityAllDestinationGuideBinding

class AllDestinationGuide : AppCompatActivity() {

    private val binding by lazy{ActivityAllDestinationGuideBinding.inflate(layoutInflater)}
    private val detailPlaceViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(DetailPlaceViewModel::class.java)
    }
    private val detailGuideViewModel by lazy { ViewModelProvider(this, ViewModelFactory.getInstance(this)
        ).get(DetailGuideViewModel::class.java)
    }
    private lateinit var destinationAdapter: DestinationGuideAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getDataPlace()
        back()
        showRecyclerView()
    }

    private fun getDataPlace() {
        val listPlaceToAdapter = ArrayList<TourismDataEntity?>()
        detailGuideViewModel.getNodes().observe(this){ resultGuide ->

            resultGuide.forEachIndexed { index, _ ->
                if (resultGuide[index].idPlace != 0){
                    detailGuideViewModel.getDetailPlace(resultGuide[index].idPlace).observe(this){ detail ->
                        Log.d("destinationdetail", detail?.title.toString())
                        listPlaceToAdapter.add(detail)
                        destinationAdapter.setData(listPlaceToAdapter)
                        Log.d("destinationlist", listPlaceToAdapter.toString())
                    }
                }
            }
        }

    }

    private fun showRecyclerView() {
        destinationAdapter = DestinationGuideAdapter()
        binding.rvGuideDestination.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@AllDestinationGuide)
            adapter = destinationAdapter
        }
    }


    private fun back() {
        binding.btnGuideDestinationBack.setOnClickListener {
            finish()
        }
    }

}