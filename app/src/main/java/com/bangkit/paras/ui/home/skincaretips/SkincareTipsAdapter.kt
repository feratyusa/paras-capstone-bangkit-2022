package com.bangkit.paras.ui.home.skincaretips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.databinding.SkincareTipsCardBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class SkincareTipsAdapter(private val listTips: List<SkincareTipsItem>): RecyclerView.Adapter<SkincareTipsAdapter.ListViewHolder>()  {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: SkincareTipsItem?)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: SkincareTipsCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = SkincareTipsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listTips[position]
        Glide.with(holder.itemView.context)
            .load(item.image)
            .transform(MultiTransformation(CenterCrop(),RoundedCorners(40)))
            .into(holder.binding.skincareTipsThumbnail)
        holder.binding.skincareTipsTitle.text=item.title
        holder.binding.skincareTipsDesc.text=item.description

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(item)
        }
    }

    override fun getItemCount(): Int = listTips.size
}