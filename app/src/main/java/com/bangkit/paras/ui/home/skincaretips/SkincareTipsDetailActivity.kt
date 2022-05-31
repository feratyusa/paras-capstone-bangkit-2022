package com.bangkit.paras.ui.home.skincaretips

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.databinding.ActivitySkincareTipsDetailBinding
import com.bumptech.glide.Glide

class SkincareTipsDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySkincareTipsDetailBinding
    private lateinit var rvTips: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkincareTipsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        rvTips=binding.recyclerViewTips
        rvTips.layoutManager= object : LinearLayoutManager(this){ override fun canScrollVertically(): Boolean { return false }}
        setupData()

    }

    private fun setupData() {
        val tips = intent.getParcelableExtra<SkincareTipsItem>("tips")
        if (tips!=null){
            Glide.with(applicationContext)
                .load(tips.image)
                .into(binding.imageThumbnail)
            binding.titleTextView.text = tips.title
            binding.detailTextView.text = tips.description
            binding.sourceTextView.text=tips.source
            binding.dateTextView.text=tips.date

            val tipsAdapter = DetailTipsAdapter(tips.tips)
            rvTips.adapter=tipsAdapter

                Log.d("TAG", "setupData: ${tips.tips}")


        }
        else {
            Glide.with(applicationContext)
                .load(tips?.image)
                .into(binding.imageThumbnail)
            binding.titleTextView.text = tips?.title
            binding.detailTextView.text = tips?.description

        }



    }
}