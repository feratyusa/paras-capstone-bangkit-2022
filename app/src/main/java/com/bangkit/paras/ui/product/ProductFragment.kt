package com.bangkit.paras.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.R
import com.bangkit.paras.databinding.FragmentProductBinding
import com.bangkit.paras.ui.product.product_detail.ProductDetailActivity
import com.google.android.material.chip.Chip


class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding


    private lateinit var rvProduct: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentProductBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvProduct = binding.rvProduct
        rvProduct.layoutManager = GridLayoutManager(requireActivity(), 2)
        val list = listOf(Product("1"), Product("2"), Product("3"), Product("4"), Product("5"))
        val productAdapter = ProductAdapter(list)
        rvProduct.adapter = productAdapter

        productAdapter.setOnItemClickCallback(object : ProductAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Product?) {

                val myIntent = Intent(requireContext(), ProductDetailActivity::class.java)
                requireContext().startActivity(myIntent)
            }
        })

        addChip("Acne")
        addChip("Oil")

    }

    private fun addChip(name: String) {
        val chip = layoutInflater.inflate(R.layout.product_category_chip,
            binding.chipProductFilter,
            false) as Chip
        chip.text = (name)
        binding.chipProductFilter.addView(chip)
    }
}