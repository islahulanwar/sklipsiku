package com.tflite.DeteksipenyakittanamanMn

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList

class KlasifikasiDariGaleri(assetManager: AssetManager, modelPath: String, labelPath: String, inputSize: Int) {
    private var interpreter: Interpreter
    private var labellist: List<String>
    private val inputsize: Int = inputSize
    private val pixelsize: Int = 3
    private val maxresults = 1
    private val threshold = 0.75f

    data class Recognition(
        var id: String = "",
        var title: String = "",
        var confidence: Float = 0F,
        val percent: Float = confidence * 100
    ) {
        override fun toString(): String {
            return "Title = $title, Prediksi = $percent%"
        }
    }

    init {
        interpreter = Interpreter(loadModelFile(assetManager, modelPath))
        labellist = loadLabelList(assetManager, labelPath)
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): MappedByteBuffer {
        val fileDescriptor = assetManager.openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun loadLabelList(assetManager: AssetManager, labelPath: String): List<String> {
        return assetManager.open(labelPath).bufferedReader().useLines { it.toList() }
    }

    fun recognizeImage(bitmap: Bitmap): List<Recognition> {
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, inputsize, inputsize, false)
        val byteBuffer = convertBitmapToByteBuffer(scaledBitmap)
        val result = Array(1) { FloatArray(labellist.size) }
        interpreter.run(byteBuffer, result)
        return getSortedResult(result)
    }

    private fun convertBitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val byteBuffer = ByteBuffer.allocateDirect(4 * inputsize * inputsize * pixelsize)
        byteBuffer.order(ByteOrder.nativeOrder())
        val intValues = IntArray(inputsize * inputsize)

        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        var pixel = 0
        for (i in 0 until inputsize) {
            for (j in 0 until inputsize) {
                val `val` = intValues[pixel++]

                // Normalisasi [0, 255] -> [0, 1]
                byteBuffer.putFloat((`val`.shr(16) and 0xFF) / 255.0f) // Red
                byteBuffer.putFloat((`val`.shr(8) and 0xFF) / 255.0f)  // Green
                byteBuffer.putFloat((`val` and 0xFF) / 255.0f)         // Blue
            }
        }
        return byteBuffer
    }

    private fun getSortedResult(labelProbArray: Array<FloatArray>): List<Recognition> {
        Log.d("KlasifikasiDariGaleri", "List Size: (${labelProbArray.size}, ${labelProbArray[0].size}, ${labellist.size})")

        val pq = PriorityQueue(
            maxresults,
            Comparator<Recognition> { (_, _, confidence1), (_, _, confidence2) ->
                confidence1.compareTo(confidence2) * -1
            }
        )

        for (i in labellist.indices) {
            val confidence = labelProbArray[0][i]
            if (confidence >= threshold) {
                pq.add(
                    Recognition(
                        id = "$i",
                        title = labellist[i],
                        confidence = confidence
                    )
                )
            } else {
                pq.add(
                    Recognition(
                        id = "$i",
                        title = "Objek tidak dikenal",
                        confidence = confidence
                    )
                )
            }
        }

        Log.d("KlasifikasiDariGaleri", "pqsize: ${pq.size}")

        val recognitions = ArrayList<Recognition>()

        val recognitionsSize = pq.size.coerceAtMost(maxresults)
        for (i in 0 until recognitionsSize) {
            pq.poll()?.let { recognition ->
                recognitions.add(recognition)
            }
        }
        return recognitions
    }
}