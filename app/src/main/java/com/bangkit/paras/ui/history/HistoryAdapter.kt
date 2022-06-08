package com.bangkit.paras.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.data.remote.response.HistoryResponse
import com.bangkit.paras.databinding.ScanHistoryCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class HistoryAdapter : ListAdapter<HistoryResponse, HistoryAdapter.ListViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ScanHistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    class ListViewHolder(var binding: ScanHistoryCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(history: HistoryResponse) {
            Glide.with(itemView)
                .load("https://upload.wikimedia.org/wikipedia/commons/3/33/Mark_Kassen%2C_Tony_C%C3%A1rdenas_and_Chris_Evans_%28cropped%29.jpg")
                .transform(MultiTransformation(CenterCrop(), RoundedCorners(40)))
                .into(binding.scanHistoryThumbnail)
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