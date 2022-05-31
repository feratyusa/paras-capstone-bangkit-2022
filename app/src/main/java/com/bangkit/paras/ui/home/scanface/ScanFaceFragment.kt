package com.bangkit.paras.ui.home.scanface

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.paras.R
import com.bangkit.paras.databinding.FragmentScanFaceBinding
import com.bangkit.paras.ui.register.RegisterActivity
import com.bangkit.paras.ui.scan.ScanActivity


class ScanFaceFragment : Fragment() {
    private lateinit var binding:FragmentScanFaceBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScanFaceBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imageView.setOnClickListener{
            val myIntent = Intent(requireContext(), ScanActivity::class.java)
            requireContext().startActivity(myIntent)
        }
    }

}