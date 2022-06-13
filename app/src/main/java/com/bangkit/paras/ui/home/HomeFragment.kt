package com.bangkit.paras.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bangkit.paras.R
import com.bangkit.paras.databinding.FragmentHomeBinding
import com.bangkit.paras.ui.home.scanface.ScanFaceFragment
import com.bangkit.paras.ui.home.skincaretips.SkincareTipsFragment


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mFragmentManager = childFragmentManager

        val mSkincareTips = SkincareTipsFragment()
        val skinTipsFragment =
            mFragmentManager.findFragmentByTag(SkincareTipsFragment::class.java.simpleName)
        if (skinTipsFragment !is SkincareTipsFragment) {
            mFragmentManager
                .beginTransaction()
                .add(R.id.flSkincareTipsFragment,
                    mSkincareTips,
                    SkincareTipsFragment::class.java.simpleName)
                .commit()
        }

        val mScanFace = ScanFaceFragment()
        val scanFaceFragment =
            mFragmentManager.findFragmentByTag(ScanFaceFragment::class.java.simpleName)
        if (scanFaceFragment !is ScanFaceFragment) {
            mFragmentManager
                .beginTransaction()
                .add(R.id.flCameraFragment, mScanFace, ScanFaceFragment::class.java.simpleName)
                .commit()
        }
    }


}