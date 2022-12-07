package com.smart.smartbalibackpaker.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.smart.smartbalibackpaker.databinding.FragmentTouristPlaceBinding
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status

class TouristPlaceFragment : Fragment() {

    private var _binding: FragmentTouristPlaceBinding? = null
    private val binding get() = _binding
    private val placeViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.getInstance(requireActivity())
        ).get(PlaceViewModel::class.java)
    }
    private var adapter = PlaceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTouristPlaceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBarTouristPlace(true)

        getTouristPlaceData()

        showRecyclerViewTouristPlace()
    }

    private fun showRecyclerViewTouristPlace() {
        binding?.apply {
            rvTourist.layoutManager = GridLayoutManager(context, 2)
            rvTourist.setHasFixedSize(true)
            rvTourist.isNestedScrollingEnabled = true
            rvTourist.adapter = adapter
        }
    }

    private fun showProgressBarTouristPlace(status: Boolean) {
        binding?.apply {
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
        Log.d("INIDATA1", "KEPANGGIL")
        placeViewModel.getPlaces("tour").observe(viewLifecycleOwner, {
            Log.d("INIDATA2", it.data.toString())

            if (it != null) {
                when (it.status) {
                    Status.LOADING -> showProgressBarTouristPlace(true)
                    Status.SUCCESS -> {
                        adapter.submitList(it.data)
                        Log.d("INI DATA2", it.data.toString())
                        showProgressBarTouristPlace(false)
                    }
                    Status.ERROR -> {
                        showProgressBarTouristPlace(false)
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}