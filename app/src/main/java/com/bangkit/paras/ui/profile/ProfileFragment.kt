package com.bangkit.paras.ui.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.bangkit.paras.MainActivity
import com.bangkit.paras.MainViewModel
import com.bangkit.paras.R
import com.bangkit.paras.data.Result
import com.bangkit.paras.data.remote.response.Credentials
import com.bangkit.paras.databinding.BottomSheetProfileBinding
import com.bangkit.paras.databinding.FragmentProfileBinding
import com.bangkit.paras.di.DataStoreManager
import com.bangkit.paras.ui.login.LoginActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private var isLogin = true

    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding=FragmentProfileBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.getUserProfile().observe(LifecycleOwner { lifecycle }){ result ->
            if(result!=null){
                when(result){
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        binding.tvUsernameContent.text=result.data.username
                        binding.tvPhoneContent.text=result.data.handphone
                        binding.tvNameContent.text=result.data.email
                        Glide.with(this)
                            .load(result.data.photo)
                            .circleCrop()
                            .into(binding.profileThumbnail)
                        Log.d("TAG", "onViewCreated: ${result.data}")
                    }
                    is Result.Error -> {

                    }
                }
            }
        }

        binding.cardName.setOnClickListener{
            setName()
        }
        binding.cardPhone.setOnClickListener{
            setPhone()
        }
        binding.btnLogout.setOnClickListener {
            runBlocking {
                model.dataStoreManager.setUser(Credentials("",""))
                val myIntent = Intent(activity, LoginActivity::class.java)
                activity?.startActivity(myIntent)
                activity?.finish()
            }
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