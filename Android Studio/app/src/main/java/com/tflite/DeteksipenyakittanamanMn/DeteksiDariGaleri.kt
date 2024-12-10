package com.tflite.DeteksipenyakittanamanMn

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.tflite.DeteksipenyakittanamanMn.databinding.ActivityImportGalleryBinding
import java.io.IOException
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher



class DeteksiDariGaleri : AppCompatActivity() {
    private lateinit var binding: ActivityImportGalleryBinding
    private lateinit var mClassifier: KlasifikasiDariGaleri
    private lateinit var mBitmap: Bitmap
    private lateinit var klasifikasiDariGaleri: KlasifikasiDariGaleri
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var editImageLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageView: ImageView
    private val mGalleryRequestCode = 2
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

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityImportGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mClassifier = KlasifikasiDariGaleri(assets, mModelPath, mLabelPath, mInputSize)

        // Memuat gambar sampel
        resources.assets.open(mSamplePath).use {
            mBitmap = BitmapFactory.decodeStream(it)
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
            binding.mPhotoImageView.setImageBitmap(mBitmap)
        }

        binding.mGalleryButton.setOnClickListener {
            val callGalleryIntent = Intent(Intent.ACTION_PICK)
            callGalleryIntent.type = "image/*"
            startActivityForResult(callGalleryIntent, mGalleryRequestCode)
        }
        binding.mDetectButton.setOnClickListener {
            val startTime = SystemClock.uptimeMillis() // Menghitung waktu awal
            val results = mClassifier.recognizeImage(mBitmap).firstOrNull()
            binding.mResultTextView.text = results?.title + "\n Probabilitas: " + results?.percent + "%"
            lastProcessingTimeMs = SystemClock.uptimeMillis() - startTime // Menghitung lamanya proses
            val waktu = lastProcessingTimeMs.toString() // Konversi ke string
            binding.delaytime.text = "$waktu ms "
        }
    }

    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall", "SetTextI18n")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == mGalleryRequestCode) {
            if (data != null) {
                val uri = data.data

                try {
                    mBitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                println("Selesai!")
                mBitmap = scaleImage(mBitmap)
                binding.mPhotoImageView.setImageBitmap(mBitmap)

            }
        } else {
            Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_LONG).show()
        }
    }

    // Fungsi untuk menyesuaikan ukuran gambar
    fun scaleImage(bitmap: Bitmap?): Bitmap {
        val originalWidth = bitmap!!.width
        val originalHeight = bitmap.height
        val scaleWidth = mInputSize.toFloat() / originalWidth
        val scaleHeight = mInputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true)
    }
}

