package com.bangkit.paras.ui.scan

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bangkit.paras.data.Result
import com.bangkit.paras.databinding.ActivityScanBinding
import com.bangkit.paras.databinding.BottomSheetScanBinding
import com.bangkit.paras.machinelearning.FaceDetection
import com.bangkit.paras.ui.ViewModelFactory
import com.bangkit.paras.utils.CameraUtilities.createTempFile
import com.bangkit.paras.utils.CameraUtilities.rotateBitmap
import com.bangkit.paras.utils.CameraUtilities.uriToFile
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileOutputStream

class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val viewModel: ScanViewModel by viewModels {
        factory
    }
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isCameraPermissionGranted = false
    private var getFile: File? = null

    private val modelPath = "model.tflite"
    private lateinit var faceDetection: FaceDetection

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        faceDetection = FaceDetection(assets, modelPath)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Upload Photo"
        }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                isCameraPermissionGranted =
                    permissions[Manifest.permission.CAMERA] ?: isCameraPermissionGranted
            }
        requestPermission()
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnUpload.setOnClickListener {
            if (getFile != null) {
                detectFace()
            } else
                Toast.makeText(
                    this,
                    "Please insert image using camera or gallery",
                    Toast.LENGTH_SHORT
                )
                    .show()
        }
    }

    private fun startCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCamera.launch(intent)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_RESULT) {
            val photo = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = photo
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )
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

    private fun uploadImage() {
        viewModel.uploadScan(getFile).observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {

                    }
                    is Result.Success -> {
                        val dialog = BottomSheetDialog(this)
                        val sheetBinding = BottomSheetScanBinding.inflate(layoutInflater)
                        dialog.setContentView(sheetBinding.root)
                        sheetBinding.bottomScanTitle.text = "Normal Face"
                        sheetBinding.bottomScanDescription.text =
                            "There’s 97% chance you have acne in your face"
                        sheetBinding.bottomScanBack.setOnClickListener {
                            dialog.dismiss()
                        }
                        dialog.show()
                    }
                    is Result.Error -> {
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }

    private fun requestPermission() {
        isCameraPermissionGranted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val permissionRequest: MutableList<String> = ArrayList()

        if (!isCameraPermissionGranted)
            permissionRequest.add(Manifest.permission.CAMERA)

        if (permissionRequest.isNotEmpty())
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

    companion object {
        const val CAMERA_RESULT = 200
    }

    private fun detectFace() {
        val filePath = getFile!!.path
        val bitmap = BitmapFactory.decodeFile(filePath)
        val result = faceDetection.detectFace(bitmap)

        if (result.confidence > 0.5f) {
            binding.scanThumbnail.setImageBitmap(result.bitmap)
            try {
                val file = createTempFile(this)
                val stream = FileOutputStream(file)
                result.bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()

                getFile = File(file.absolutePath)
                uploadImage()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error while processing image", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please use an image of a face", Toast.LENGTH_SHORT).show()
        }
    }
}