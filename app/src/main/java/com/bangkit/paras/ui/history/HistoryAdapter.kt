package com.bangkit.paras.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.databinding.ScanHistoryCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class HistoryAdapter (private val listHistory: List<ScanItem>): RecyclerView.Adapter<HistoryAdapter.ListViewHolder>()  {

    class ListViewHolder(var binding: ScanHistoryCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ScanHistoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listHistory[position]
        Glide.with(holder.itemView.context)
            .load(item.image)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(40)))
            .into(holder.binding.scanHistoryThumbnail)
        holder.binding.scanHistoryDate.text = item.date
        holder.binding.scanHistoryTitle.text=item.title
        holder.binding.scanHistoryDesc.text=item.description
    }

    override fun getItemCount(): Int = listHistory.size
}