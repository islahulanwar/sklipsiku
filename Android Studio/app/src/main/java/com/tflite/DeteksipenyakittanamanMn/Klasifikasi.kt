package com.tflite.DeteksipenyakittanamanMn

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.nio.ByteBuffer
import java.nio.ByteOrder

class Klasifikasi(assetManager: AssetManager) {

    private val labels: List<String>
    private val model: Interpreter

    init {
        // Menginisialisasi Interpreter dengan opsi
        val options = Interpreter.Options().apply {
            setNumThreads(4) // Menyesuaikan jumlah thread
            setUseXNNPACK(true) // Optimasi performa
        }
        model = Interpreter(loadModelFile(assetManager, MODEL_PATH), options)

        // Muat label menggunakan AssetManager
        labels = assetManager.open(LABELS_PATH).bufferedReader().useLines { lines ->
            lines.toList()
        }
    }

    fun recognize(data: ByteArray): List<Deteksi> {
        val unscaledBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        val bitmap = Bitmap.createScaledBitmap(unscaledBitmap, MODEL_INPUT_SIZE, MODEL_INPUT_SIZE, false)

        // Preprocessing input
        val inputImage = preprocessInput(bitmap)

        // Buat buffer untuk output
        val outputBuffer = TensorBuffer.createFixedSize(
            intArrayOf(BATCH_SIZE, labels.size),
            org.tensorflow.lite.DataType.FLOAT32
        )

        // Jalankan model
        model.run(inputImage.buffer, outputBuffer.buffer)

        return parseResults(outputBuffer.floatArray)
    }

    private fun preprocessInput(bitmap: Bitmap): TensorImage {
        val tensorImage = TensorImage(org.tensorflow.lite.DataType.FLOAT32)
        tensorImage.load(bitmap)
        return tensorImage
    }

    private fun parseResults(result: FloatArray): List<Deteksi> {
        return labels.mapIndexed { index, label ->
            Deteksi(label, result[index])
        }.sortedByDescending { it.probability }
    }

    private fun loadModelFile(assetManager: AssetManager, modelPath: String): ByteBuffer {
        return assetManager.open(modelPath).use {
            val buffer = ByteBuffer.allocateDirect(it.available())
            buffer.order(ByteOrder.nativeOrder())
            buffer.put(it.readBytes())
            buffer.flip()
            buffer
        }
    }

    companion object {
        private const val BATCH_SIZE = 1
        private const val MODEL_INPUT_SIZE = 224
        private const val LABELS_PATH = "label.txt"
        private const val MODEL_PATH = "DeteksiPenyakitTanamanMoNet.tflite"
    }
}
