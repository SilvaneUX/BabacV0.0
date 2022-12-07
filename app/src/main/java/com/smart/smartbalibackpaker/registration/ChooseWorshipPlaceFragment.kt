package com.smart.smartbalibackpaker.registration

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status
import com.smart.smartbalibackpaker.dashboard.PlaceViewModel
import com.smart.smartbalibackpaker.databinding.FragmentChoosePlaceBinding
import com.smart.smartbalibackpaker.databinding.FragmentChooseTouristPlaceBinding
import com.smart.smartbalibackpaker.databinding.FragmentChooseWorshipPlaceBinding
import com.smart.smartbalibackpaker.databinding.FragmentTouristPlaceBinding

class ChooseWorshipPlaceFragment : Fragment() {
    private var _binding: FragmentChooseWorshipPlaceBinding? = null
    private val binding get() = _binding
    private val placeViewModel by lazy {
        ViewModelProvider(
            this, ViewModelFactory.getInstance(requireActivity())
        ).get(PlaceViewModel::class.java)
    }
    private var adapter = ChoosePlaceAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChooseWorshipPlaceBinding.inflate(inflater, container, false)
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
            rvWorship.layoutManager = GridLayoutManager(context, 1)
            rvWorship.setHasFixedSize(true)
            rvWorship.isNestedScrollingEnabled = true
            rvWorship.adapter = adapter
        }
    }

    private fun showProgressBarTouristPlace(status: Boolean) {
        binding?.apply {
            if (status) {
                progressbarTourist.visibility = View.VISIBLE
                rvWorship.visibility = View.GONE
            } else {
                progressbarTourist.visibility = View.GONE
                rvWorship.visibility = View.VISIBLE
            }
        }
    }

    private fun getTouristPlaceData() {
        placeViewModel.getPlaces("worship").observe(viewLifecycleOwner, {
            Log.d("INI DATA1", it.data.toString())

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