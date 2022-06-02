package com.bangkit.paras.ui.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.databinding.FragmentHistoryBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var rvHistory: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory=binding.rvHistory
        rvHistory.layoutManager= LinearLayoutManager(requireActivity())
        val list = getScanHistory(context)
        val historyAdapter = HistoryAdapter(list)
        rvHistory.adapter=historyAdapter
    }

    private fun getScanHistory(context: Context?): List<ScanItem> {
        lateinit var jsonString: String
        try {
            if (context != null) {
                jsonString = context.assets.open("scanHistory.json")
                    .bufferedReader()
                    .use { it.readText() }
            }
        } catch (ioException: IOException) {
            Log.d("HistoryFragment", "getScanHistory: $ioException")
        }

        val listHistory = object : TypeToken<List<ScanItem>>() {}.type
        return Gson().fromJson(jsonString, listHistory)
    }

}