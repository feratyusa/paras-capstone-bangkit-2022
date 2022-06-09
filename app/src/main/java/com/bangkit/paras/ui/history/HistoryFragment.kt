package com.bangkit.paras.ui.history

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.paras.MainViewModel
import com.bangkit.paras.data.Result
import com.bangkit.paras.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var rvHistory: RecyclerView
    private val model: MainViewModel by activityViewModels()
    private val historyAdapter = HistoryAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory = binding.rvHistory
        rvHistory.layoutManager = LinearLayoutManager(requireActivity())
        rvHistory.adapter = historyAdapter
        getScanHistory(context)
        binding.progressBar.visibility = View.INVISIBLE

    }

    private fun getScanHistory(context: Context?) {
        model.getUserHistory().observe({ lifecycle }) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE

                    }
                    is Result.Success -> {
                        val data = result.data
                        val list = data.sortedByDescending { it.date }
                        historyAdapter.submitList(list)
                        if (data.isEmpty()) {
                            binding.apply {
                                historyTitle.visibility = View.GONE
                                rvHistory.visibility = View.GONE
                                noHistoryLogo.visibility = View.VISIBLE
                                tvNoHistory.visibility = View.VISIBLE
                            }
                        } else {
                            binding.apply {
                                historyTitle.visibility = View.VISIBLE
                                rvHistory.visibility = View.VISIBLE
                                noHistoryLogo.visibility = View.GONE
                                tvNoHistory.visibility = View.GONE
                            }

                        }
                        Log.d("TAG", "onViewCreated: ${result.data}")
                        binding.progressBar.visibility = View.INVISIBLE
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE

                    }
                }
            }
        }

    }

}