package com.smart.smartbalibackpaker.core.model.favorite

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.smart.smartbalibackpaker.DetailActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.databinding.ItemGridPlaceBinding

class FavoritePlacesAdapter : RecyclerView.Adapter<FavoritePlacesAdapter.ListViewHolder>() {

    private val place = ArrayList<TourismDataEntity>()

    fun setFavoriteList(list: List<TourismDataEntity>){
        place.clear()
        place.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return place.size
    }

    class ListViewHolder(val binding: ItemGridPlaceBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(dataPlace: TourismDataEntity){
            with(binding){
                tvTitlePlace.text = dataPlace.title
                tvLocationPlace.text = dataPlace.address

                Glide.with(itemView.context)
                    .load("https://smart-balibackpacker.com/storage/public/pictures/thumbnail/${dataPlace.thumbnail}")
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .apply(RequestOptions().transform(RoundedCorners(10)).override(90, 120))
                    .into(ivPlace)

                itemView.setOnClickListener {
                    val intent = Intent(itemView.context, DetailActivity::class.java)
                    intent.apply {
                        putExtra(DetailActivity.EXTRA_ID, dataPlace.id)
                        putExtra(DetailActivity.EXTRA_TYPE, dataPlace.type)
                    }
                    it.context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemGridPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(place[position])
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TourismDataEntity>(){
            override fun areItemsTheSame(
                oldItem: TourismDataEntity,
                newItem: TourismDataEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TourismDataEntity,
                newItem: TourismDataEntity
            ): Boolean {
                return  oldItem == newItem
            }
        }
    }

}