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
import com.smart.smartbalibackpaker.databinding.FragmentHotelVillaBinding
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status


class HotelVillaFragment : Fragment() {

    private var _binding: FragmentHotelVillaBinding? = null
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
        _binding = FragmentHotelVillaBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBarHotelPlace(true)

        getHotelPlaceData()

        showRecyclerViewHotelPlace()
    }

    private fun showRecyclerViewHotelPlace() {
        binding?.apply {
            rvHotel.layoutManager = GridLayoutManager(context, 2)
            rvHotel.setHasFixedSize(true)
            rvHotel.isNestedScrollingEnabled = true
            rvHotel.adapter = adapter
        }
    }

    private fun showProgressBarHotelPlace(status: Boolean) {
        binding?.apply {
            if (status) {
                progressbarHotel.visibility = View.VISIBLE
                rvHotel.visibility = View.GONE
            } else {
                progressbarHotel.visibility = View.GONE
                rvHotel.visibility = View.VISIBLE
            }
        }
    }

    private fun getHotelPlaceData() {
        placeViewModel.getPlaces("hotel").observe(viewLifecycleOwner, {
            Log.d("INI DATA1", it.data.toString())

            if (it != null) {
                when (it.status) {
                    Status.LOADING -> showProgressBarHotelPlace(true)
                    Status.SUCCESS -> {
                        adapter.submitList(it.data)
                        Log.d("INI DATA2", it.data.toString())
                        showProgressBarHotelPlace(false)
                    }
                    Status.ERROR -> {
                        showProgressBarHotelPlace(false)
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}