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
import com.smart.smartbalibackpaker.databinding.FragmentWorshipPlaceBinding
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.core.vo.Status

class WorshipPlaceFragment : Fragment() {

    private var _binding: FragmentWorshipPlaceBinding? = null
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
        _binding = FragmentWorshipPlaceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showProgressBarWorshipPlace(true)

        getWorshipPlaceData()

        showRecyclerViewWorshipPlace()
    }

    private fun showRecyclerViewWorshipPlace() {
        binding?.apply {
            rvWorship.layoutManager = GridLayoutManager(context, 2)
            rvWorship.setHasFixedSize(true)
            rvWorship.isNestedScrollingEnabled = true
            rvWorship.adapter = adapter
        }
    }

    private fun showProgressBarWorshipPlace(status: Boolean) {
        binding?.apply {
            if (status) {
                progressbarWorship.visibility = View.VISIBLE
                rvWorship.visibility = View.GONE
            } else {
                progressbarWorship.visibility = View.GONE
                rvWorship.visibility = View.VISIBLE
            }
        }
    }

    private fun getWorshipPlaceData() {
        placeViewModel.getPlaces("worship").observe(viewLifecycleOwner, {
            Log.d("WORSHIP", it.data.toString())

            if (it != null) {
                when (it.status) {
                    Status.LOADING -> showProgressBarWorshipPlace(true)
                    Status.SUCCESS -> {
                        adapter.submitList(it.data)
                        showProgressBarWorshipPlace(false)
                    }
                    Status.ERROR -> {
                        showProgressBarWorshipPlace(false)
                        Toast.makeText(requireActivity(), "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

}