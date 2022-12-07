package com.smart.smartbalibackpaker.registration

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.smart.smartbalibackpaker.core.data.source.local.entity.ListRegistPlaceEntity
import com.smart.smartbalibackpaker.core.data.source.local.entity.TourismDataEntity
import com.smart.smartbalibackpaker.databinding.ItemGridRegistPlaceBinding
import kotlinx.android.synthetic.main.item_grid_regist_place.view.*

class ListRegistPlaceAdapter(private val listPlaces: ArrayList<ListRegistPlaceEntity>) : RecyclerView.Adapter<ListRegistPlaceAdapter.ListViewHolder>() {

    private var onClickListener : OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int){
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemGridRegistPlaceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listPlaces[position])
        holder.itemView.ic_remove_list.setOnClickListener {
            if(onClickListener != null){
                onClickListener!!.onClick(position)
                removeItem(position)
            }
        }
    }

    private fun removeItem(position: Int){
        listPlaces.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = listPlaces.size

    class ListViewHolder(itemView: ItemGridRegistPlaceBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val tvTitlePlace = itemView.tvTitlePlace
        private val icRemove = itemView.icRemoveList
        fun bind(list: ListRegistPlaceEntity) {
            with(itemView){
                tvTitlePlace.text = list.place
            }
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