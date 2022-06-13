package com.bangkit.paras.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bangkit.paras.data.remote.response.HistoryResponse
import com.bangkit.paras.databinding.ScanHistoryCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class HistoryAdapter : ListAdapter<HistoryResponse, HistoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    private lateinit var circularProgressDrawable: CircularProgressDrawable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ScanHistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        circularProgressDrawable = CircularProgressDrawable(parent.context)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val data = getItem(position)
        if (data != null) {
            holder.bind(data, circularProgressDrawable)
        }
    }

    class ListViewHolder(var binding: ScanHistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryResponse, circularProgressDrawable: CircularProgressDrawable) {
            if (history.image != null) {
                Glide.with(itemView)
                    .load(history.image)
                    .placeholder(circularProgressDrawable)
                    .transform(MultiTransformation(CenterCrop(), RoundedCorners(40)))
                    .into(binding.scanHistoryThumbnail)
            }
            binding.scanHistoryDate.text = history.date
            binding.scanHistoryTitle.text = history.symptom
            binding.scanHistoryDesc.text = "Description"
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<HistoryResponse>() {
            override fun areItemsTheSame(
                oldItem: HistoryResponse,
                newItem: HistoryResponse,
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: HistoryResponse,
                newItem: HistoryResponse,
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}