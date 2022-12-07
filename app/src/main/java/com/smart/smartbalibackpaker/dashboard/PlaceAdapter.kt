package com.smart.smartbalibackpaker.dashboard

import android.content.Intent
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

class PlaceAdapter : PagedListAdapter<TourismDataEntity, PlaceAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(itemView: ItemGridPlaceBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val imgPhoto = itemView.ivPlace
        private val tvTitle = itemView.tvTitlePlace
        private val tvLocation = itemView.tvLocationPlace
        fun bind(place: TourismDataEntity) {
            tvTitle.text = place.title
            tvLocation.text = place.address

            Glide.with(itemView.context)
                .load("https://balibackpacker.co.id/storage/public/pictures/thumbnail/${place.thumbnail}")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .apply(RequestOptions().transform(RoundedCorners(10)).override(90, 120))
                .into(imgPhoto)
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.apply {
                    putExtra(DetailActivity.EXTRA_ID, place.id)
                    putExtra(DetailActivity.EXTRA_TYPE, place.type)
                }
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemGridPlaceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<TourismDataEntity>() {
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
                return oldItem == newItem
            }
        }
    }

}