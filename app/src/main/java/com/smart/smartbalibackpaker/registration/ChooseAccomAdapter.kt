package com.smart.smartbalibackpaker.registration

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.smart.smartbalibackpaker.MainActivity
import com.smart.smartbalibackpaker.R
import com.smart.smartbalibackpaker.core.data.source.local.entity.AccomDataEntity
import com.smart.smartbalibackpaker.core.utils.Formatter
import com.smart.smartbalibackpaker.core.viewmodel.ViewModelFactory
import com.smart.smartbalibackpaker.databinding.ItemGridChoosePlaceBinding

class ChooseAccomAdapter: PagedListAdapter<AccomDataEntity, ChooseAccomAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder(itemView: ItemGridChoosePlaceBinding) : RecyclerView.ViewHolder(itemView.root) {
//        private val registSecondFormViewModel by lazy {
//            ViewModelProvider(
//                this, ViewModelFactory.getInstance(this)
//            ).get(RegistSecondFormViewModel::class.java)
//        }


        private val imgPhoto = itemView.ivPlace
        private val tvTitle = itemView.tvTitlePlace
        private val tvLocation = itemView.tvLocationPlace
        private val tvPlaceDistance = itemView.tvPlaceDistance
        fun bind(accom: AccomDataEntity) {
            tvTitle.text = accom.name
            tvLocation.text = accom.no_car
            tvPlaceDistance.text = accom.rent_price?.let { Formatter.rupiahFormatter(it.toDouble()) }

            Glide.with(itemView.context)
                .load("https://balibackpacker.co.id/storage/public/pictures/galleries/${accom.pictures}")
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .apply(RequestOptions().transform(RoundedCorners(10)).override(90, 120))
                .into(imgPhoto)
            itemView.setOnClickListener {
                val sourceIntent = (it.context as Activity).intent

                val intent = Intent(itemView.context, ChooseAccomActivity::class.java)
                Log.d("DATAMOBILID", accom.id.toString())
                intent.putExtra(ChooseAccomActivity.EXTRA_EMAIL, sourceIntent.getStringExtra(ChooseAccomActivity.EXTRA_EMAIL))
                intent.putExtra(ChooseAccomActivity.EXTRA_NAMA, sourceIntent.getStringExtra(ChooseAccomActivity.EXTRA_NAMA))
                intent.putExtra(ChooseAccomActivity.EXTRA_NOHP, sourceIntent.getStringExtra(ChooseAccomActivity.EXTRA_NOHP))
                intent.putExtra(ChooseAccomActivity.EXTRA_DATANG, sourceIntent.getStringExtra(ChooseAccomActivity.EXTRA_DATANG))
                intent.putExtra(ChooseAccomActivity.EXTRA_PERGI, sourceIntent.getStringExtra(ChooseAccomActivity.EXTRA_PERGI))
                intent.putExtra(ChooseAccomActivity.EXTRA_TEMP_DATANG, sourceIntent.getStringExtra(ChooseAccomActivity.EXTRA_TEMP_DATANG))
//                intent.putExtra(ChooseAccomActivity.EXTRA_NAME_ACCOM, "${accom.name} - ${accom.no_car} - ${accom.rent_price}")
                intent.putExtra(ChooseAccomActivity.EXTRA_ID_ACCOM, accom.id.toString())
                intent.putExtra(ChooseAccomActivity.EXTRA_NAME_CAR_ACCOM, "${accom.name} - ${accom.no_car}")
                intent.putExtra(ChooseAccomActivity.PLACE_DATA, sourceIntent.getStringExtra(ChooseAccomActivity.PLACE_DATA))

                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            ItemGridChoosePlaceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AccomDataEntity>() {
            override fun areItemsTheSame(
                oldItem: AccomDataEntity,
                newItem: AccomDataEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: AccomDataEntity,
                newItem: AccomDataEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

}