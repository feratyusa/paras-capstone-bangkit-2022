package com.bangkit.paras.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.exifinterface.media.ExifInterface
import com.bangkit.paras.databinding.ActivityScanBinding
import com.bangkit.paras.databinding.BottomSheetScanBinding
import com.bangkit.paras.utils.rotateBitmap
import com.bangkit.paras.utils.uriToFile
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream

class ScanActivity : AppCompatActivity() {
    private lateinit var binding:ActivityScanBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var currentPhotoPath: String
    private var isCameraPermissionGranted = false
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Upload Photo"
        }

        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){ permissions ->
            isCameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: isCameraPermissionGranted
        }
        requestPermission()
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {
            if(getFile!= null)
                uploadImage()
            else
                Toast.makeText(this, "Please insert image using camera or gallery", Toast.LENGTH_SHORT)
                    .show()
        }
    }

    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        com.bangkit.paras.utils.createTempFile(this).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.bangkit.paras",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            var rotate = 0
            val exif = ExifInterface(myFile.path)
            val orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL)
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> {
                    rotate = 90
                }
                ExifInterface.ORIENTATION_ROTATE_180 -> {
                    rotate = 180
                }
                ExifInterface.ORIENTATION_ROTATE_270 -> {
                    rotate = 270
                }

            }
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                rotate
            )
            val file = com.bangkit.paras.utils.createTempFile(this)
            val stream = FileOutputStream(file)
            result.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()

            getFile = File(file.absolutePath)
            binding.scanThumbnail.setImageBitmap(result)
        }
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
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri

            val myFile = uriToFile(selectedImg, this)

            getFile = myFile

            binding.scanThumbnail.setImageURI(selectedImg)
        }
    }

    private fun uploadImage(){
        val dialog = BottomSheetDialog(this)
        val sheetBinding = BottomSheetScanBinding.inflate(layoutInflater)
        dialog.setContentView(sheetBinding.root)
        sheetBinding.bottomScanTitle.text = "Normal Face"
        sheetBinding.bottomScanDescription.text = "There’s 97% chance you have acne in your face"
        sheetBinding.bottomScanBack.setOnClickListener{
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun requestPermission(){
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest : MutableList<String> = ArrayList()

        if(!isCameraPermissionGranted)
            permissionRequest.add(Manifest.permission.CAMERA)

        if(permissionRequest.isNotEmpty())
            permissionLauncher.launch(permissionRequest.toTypedArray())
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> true
        }
    }
}