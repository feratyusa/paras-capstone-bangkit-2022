package com.bangkit.paras.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bangkit.paras.R
import com.bangkit.paras.databinding.BottomSheetProfileBinding
import com.bangkit.paras.databinding.FragmentProfileBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private var isLogin = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentProfileBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardName.setOnClickListener{
            setName()
        }
        binding.cardPhone.setOnClickListener{
            setPhone()
        }
        binding.btnLogout.setOnClickListener {
            isLogin = false
        }

    }

    private fun setName(){
        val dialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomSheetProfileBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        sheetBinding.bottomProfileTitle.text = getString(R.string.edit_name)
        sheetBinding.bottomProfileCancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setPhone(){
        val dialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomSheetProfileBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        sheetBinding.bottomProfileTitle.text = getString(R.string.edit_phone)
        sheetBinding.bottomProfileCancel.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }


}