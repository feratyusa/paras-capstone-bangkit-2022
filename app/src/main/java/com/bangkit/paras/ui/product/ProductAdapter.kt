package com.bangkit.paras.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.databinding.ProductListCardBinding
import com.bumptech.glide.Glide

class ProductAdapter(private val listProduct: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Product?)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ProductListCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ProductListCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductAdapter.ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = listProduct[position]
        holder.binding.productTitle.text = "CLEAR SKIN"
        Glide.with(holder.itemView.context)
            .load("https://media.cnn.com/api/v1/images/stellar/prod/210803094851-best-skincare-products-over-40-dr-loretta.jpg?q=x_0,y_0,h_900,w_1600,c_crop/h_270,w_480")
            .into(holder.binding.skincareTipsThumbnail)
        holder.binding.productDesc.text = "Skin Ceutical"

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(item)
        }

    }

    override fun getItemCount(): Int = listProduct.size

}