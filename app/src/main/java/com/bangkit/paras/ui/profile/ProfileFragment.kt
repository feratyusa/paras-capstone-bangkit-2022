package com.bangkit.paras.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bangkit.paras.MainViewModel
import com.bangkit.paras.R
import com.bangkit.paras.data.Result
import com.bangkit.paras.data.remote.response.Credentials
import com.bangkit.paras.databinding.BottomSheetProfileBinding
import com.bangkit.paras.databinding.FragmentProfileBinding
import com.bangkit.paras.ui.login.LoginActivity
import com.bangkit.paras.utils.CameraUtilities
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var currentEmail: String? = null
    private var currentPhone: String? = null
    private var getFile: File? = null

    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.progressBar.visibility = View.INVISIBLE


        model.getUserProfile().observe({ lifecycle }) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE

                    }
                    is Result.Success -> {
                        binding.tvUsernameContent.text = result.data.username
                        binding.tvPhoneContent.text = result.data.handphone
                        binding.tvEmailContent.text = result.data.email
                        Glide.with(this)
                            .load(result.data.photo)
                            .circleCrop()
                            .into(binding.profileThumbnail)
                        currentEmail = result.data.email.toString()
                        currentPhone = result.data.handphone.toString()
                        Log.d("TAG", "onViewCreated: ${result.data}")
                        binding.progressBar.visibility = View.INVISIBLE

                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE

                    }
                }
            }
        }
        binding.profileThumbnail.setOnClickListener {
            setPhoto()
        }

        binding.cardEmail.setOnClickListener {
            setEmail()
        }
        binding.cardPhone.setOnClickListener {
            setPhone()
        }
        binding.btnLogout.setOnClickListener {
            runBlocking {
                model.dataStoreManager.setUser(Credentials("", ""))
                val myIntent = Intent(activity, LoginActivity::class.java)
                activity?.startActivity(myIntent)
                activity?.finish()
            }
        }

    }

    private fun setPhoto() {
        startGallery()
    }

    private fun setEmail() {
        val dialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomSheetProfileBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        sheetBinding.bottomProfileTitle.text = getString(R.string.edit_name)
        if (currentEmail != null)
            sheetBinding.bottomProfileEdit.setText(currentEmail)
        else
            sheetBinding.bottomProfileEdit.setText(currentPhone)

        sheetBinding.bottomProfileCancel.setOnClickListener {
            dialog.dismiss()
        }
        sheetBinding.bottomProfileSave.setOnClickListener {
            val email = sheetBinding.bottomProfileEdit.text.toString()
            model.editUserProfileEmail(email).observe({ lifecycle }) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {

                        }
                        is Result.Success -> {
                            binding.tvEmailContent.text = email
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                            Log.d("TAG", "onViewCreated: ${result.data}")
                        }
                        is Result.Error -> {
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
        dialog.show()
    }

    private fun setPhone() {
        val dialog = BottomSheetDialog(requireContext())
        val sheetBinding = BottomSheetProfileBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        sheetBinding.bottomProfileTitle.text = getString(R.string.edit_phone)
        sheetBinding.bottomProfileCancel.setOnClickListener {
            dialog.dismiss()
        }
        if (currentPhone != null)
            sheetBinding.bottomProfileEdit.setText(currentPhone)
        else
            sheetBinding.bottomProfileEdit.setText("")

        sheetBinding.bottomProfileSave.setOnClickListener {
            val phone = sheetBinding.bottomProfileEdit.text.toString()
            model.editUserProfilePhone(phone).observe({ lifecycle }) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {

                        }
                        is Result.Success -> {
                            binding.tvPhoneContent.text = phone
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                            Log.d("TAG", "onViewCreated: ${result.data}")
                        }
                        is Result.Error -> {
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT)
                                .show()
                            dialog.dismiss()
                        }
                    }
                }
            }
        }
        dialog.show()
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = CameraUtilities.uriToFile(selectedImg, requireContext())

            getFile = myFile

            model.editUserProfilePhoto(myFile).observe({ lifecycle }) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {

                        }
                        is Result.Success -> {
                            binding.profileThumbnail.setImageURI(selectedImg)
                            Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT)
                                .show()
                            Log.d("TAG", "onViewCreated: ${result.data}")
                        }
                        is Result.Error -> {
                            Toast.makeText(requireContext(), "Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }

        }
    }

}