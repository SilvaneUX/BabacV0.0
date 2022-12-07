package com.smart.smartbalibackpaker.guide

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.data.source.local.entity.RecordVacationListEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.databinding.ItemDestinationGuideBinding

class DestinationGuideAdapter() : RecyclerView.Adapter<DestinationGuideAdapter.ListViewHolder>() {

    private val places = ArrayList<TourismDataEntity?>()

    fun setData(newPlaces: ArrayList<TourismDataEntity?>){
        places.clear()
        places.addAll(newPlaces)
        notifyDataSetChanged()
    }

    class ListViewHolder(val binding : ItemDestinationGuideBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemDestinationGuideBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val place = places[position]

        holder.binding.apply {
            tvTitle.text = place?.title
            tvAddress.text = place?.address
        }

        Glide.with(holder.itemView.context)
            .load("https://balibackpacker.co.id/storage/public/pictures/thumbnail/${place?.thumbnail}")
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .apply(RequestOptions().centerCrop())
            .into(holder.binding.imgPlace)
    }

    override fun getItemCount(): Int = places.size
}