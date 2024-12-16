package com.tflite.DeteksipenyakittanamanMn

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.soundcloud.android.crop.Crop
import com.tflite.DeteksipenyakittanamanMn.databinding.ActivityImportGalleryBinding
import java.io.File
import java.io.InputStream


class DeteksiDariGaleri : AppCompatActivity() {
    private val pickImageRequest = 1
    private var sourceUri: Uri? = null
    private var destinationUri: Uri? = null
    private lateinit var binding: ActivityImportGalleryBinding
    private lateinit var mClassifier: KlasifikasiDariGaleri
    private lateinit var mBitmap: Bitmap
    private val mInputSize = 224
    private val mModelPath = "DeteksiPenyakitTanamanMoNet.tflite" // Model MobileNetV2
    private val mLabelPath = "label.txt"
    private val mSamplePath = "sample.JPG"
    private var lastProcessingTimeMs: Long = 0

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "Pendeteksi Penyakit Tanaman"
        }

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        binding = ActivityImportGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi klasifikasi model
        mClassifier = KlasifikasiDariGaleri(assets, mModelPath, mLabelPath, mInputSize)

        // Memuat gambar sampel
        resources.assets.open(mSamplePath).use {
            mBitmap = BitmapFactory.decodeStream(it)
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
            binding.mPhotoImageView.setImageBitmap(mBitmap)
        }

        binding.mGalleryButton.setOnClickListener { v ->
            // Membuka galeri untuk memilih gambar
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageRequest)
        }

        // Tombol untuk mendeteksi penyakit dari gambar
        binding.mDetectButton.setOnClickListener {
            classifyImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageRequest && resultCode == RESULT_OK && data != null) {
            sourceUri = data.data
            val croppedImageFile = File(cacheDir, "cropped")
            destinationUri = Uri.fromFile(croppedImageFile)
            Crop.of(sourceUri, destinationUri).asSquare().start(this)

        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                val croppedUri = Crop.getOutput(data)
                if (croppedUri != null) {
                    val inputStream: InputStream? = contentResolver.openInputStream(croppedUri)
                    val bitmapImage = BitmapFactory.decodeStream(inputStream)
                    if (bitmapImage != null) {
                        mBitmap = bitmapImage
                        binding.mPhotoImageView.setImageBitmap(mBitmap) // Display the cropped image
                    } else {
                        Toast.makeText(this, "Failed to decode cropped image.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Cropped image URI is null.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error displaying cropped image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk klasifikasi gambar
    private fun classifyImage() {
        val startTime = SystemClock.uptimeMillis()
        val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
        binding.mResultTextView.text =
            results?.title + "\n Probabilitas: " + results?.percent + "%"
        lastProcessingTimeMs =
            SystemClock.uptimeMillis() - startTime
        binding.delaytime.text = "$lastProcessingTimeMs ms"
    }
}
