package com.bangkit.paras.ui.home.skincaretips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.databinding.SkincareTipsDetailCardBinding
import com.bumptech.glide.Glide


class DetailTipsAdapter(private val listTips: List<TipsItem>) :
    RecyclerView.Adapter<DetailTipsAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: SkincareTipsDetailCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = SkincareTipsDetailCardBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listTips[position]

        Glide.with(holder.itemView.context)
            .load(item.image)
            .into(holder.binding.skincareTipsThumbnail)
        holder.binding.tipsTitle.text = item.tipstitle
        holder.binding.tipsDesc.text = item.description
    }

    override fun getItemCount(): Int = listTips.size


}