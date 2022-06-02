package com.bangkit.paras.machinelearning

import android.content.res.AssetManager
import android.graphics.Bitmap
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

/*
    1. create local variable
    private val modelPath = "model.tflite"
    private lateinit var faceDetection: FaceDetection

    2. init faceDetection on onCreate
    faceDetection = FaceDetection(assets, modelPath)

    3. create bitmap from image and detect face
    val result = faceDetection.detectFace(bitmap)

    4. check confidence (0.5f for now) and continue to the next progress
    if (result.confidence > 0.5f) {
        val bitmap = result.bitmap
        // Todo
    } else {
        // Todo
    }
*/

class FaceDetection(assetManager: AssetManager, modelPath: String) {
    private var interpreter: Interpreter
    private val inputSize = 200
    private val pixelSize = 3
    private val numBytesPerChannel = 4
    private val imageMean = 0
    private val imageSTD = 255.0f
    private val outputSize = 150

    init {
        val options = Interpreter.Options()
        options.numThreads = -1
        options.useNNAPI = true
        interpreter = Interpreter(loadModelFile(assetManager, modelPath), options)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength

        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer =
            ByteBuffer.allocateDirect(1 * inputSize * inputSize * pixelSize * numBytesPerChannel)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputSize * inputSize)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val input = intValues[pixel++]

                byteBuffer.putFloat((((input.shr(16) and 0xFF) - imageMean) / imageSTD))
                byteBuffer.putFloat((((input.shr(8) and 0xFF) - imageMean) / imageSTD))
                byteBuffer.putFloat((((input and 0xFF) - imageMean) / imageSTD))
            }
        }

        return byteBuffer
    }

    fun detectFace(bitmap: Bitmap): Detection {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        val inputMap = arrayOf(byteBuffer)
        val confidence = Array(1) { FloatArray(1) }
        val bbox = Array(1) { FloatArray(4) }
        val outputMap: MutableMap<Int, Any> = HashMap()
        outputMap[0] = bbox
        outputMap[1] = confidence
        interpreter.runForMultipleInputsOutputs(inputMap, outputMap)
        return cropImage(bitmap, confidence[0][0], bbox[0])
    }

    private fun cropImage(bitmap: Bitmap, confidence: Float, bbox: FloatArray): Detection {
        val x = (bbox[0] * inputSize).toInt()
        val y = (bbox[1] * inputSize).toInt()
        val width = (bbox[2] * inputSize).toInt() - x
        val height = (bbox[3] * inputSize).toInt() - y
        try {
            val croppedBitmap = Bitmap.createBitmap(
                bitmap,
                x,
                y,
                width,
                height
            )

            val resizedBitmap = Bitmap.createScaledBitmap(
                croppedBitmap,
                outputSize,
                outputSize,
                false
            )

            return Detection(
                confidence,
                resizedBitmap
            )
        } catch (e: Exception) {
            return Detection(
                0.0f,
                bitmap
            )
        }
    }
}