package com.bangkit.paras.ui.home.skincaretips

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.databinding.FragmentSkincareTipsBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class SkincareTipsFragment : Fragment() {

    private lateinit var binding:FragmentSkincareTipsBinding
    private lateinit var rvTips: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSkincareTipsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTips=binding.rvTips
        rvTips.layoutManager= LinearLayoutManager(requireActivity())
        val list = getSkincareTips(context)
        val tipsAdapter = SkincareTipsAdapter(list)
        rvTips.adapter=tipsAdapter

        tipsAdapter.setOnItemClickCallback(object : SkincareTipsAdapter.OnItemClickCallback {
            override fun onItemClicked(data: SkincareTipsItem?) {


                val myIntent = Intent(requireContext(), SkincareTipsDetailActivity::class.java)
                myIntent.putExtra("tips",data)
                requireContext().startActivity(myIntent)
            }
        })

    }

    private fun getSkincareTips(context: Context?): List<SkincareTipsItem> {
        lateinit var jsonString: String
        try {
            if (context != null) {
                jsonString = context.assets.open("skincareTips.json")
                    .bufferedReader()
                    .use { it.readText() }
            }
        } catch (ioException: IOException) {
            Log.d("SkincareTipsFragment", "getSkincareTips: ${ioException.toString()}")
        }

        val listTips = object : TypeToken<List<SkincareTipsItem>>() {}.type
        return Gson().fromJson(jsonString, listTips)
    }

}