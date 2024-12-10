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
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.soundcloud.android.crop.Crop
import com.tflite.DeteksipenyakittanamanMn.databinding.ActivityImportGalleryBinding
import java.io.File
import java.io.IOException
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
    private var selectedImageUri: Uri? = null

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cropLauncher: ActivityResultLauncher<Intent>

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

        // Inisialisasi klasifikasi model
        mClassifier = KlasifikasiDariGaleri(assets, mModelPath, mLabelPath, mInputSize)

        // Memuat gambar sampel
        resources.assets.open(mSamplePath).use {
            mBitmap = BitmapFactory.decodeStream(it)
            mBitmap = Bitmap.createScaledBitmap(mBitmap, mInputSize, mInputSize, true)
            binding.mPhotoImageView.setImageBitmap(mBitmap)
        }

//        // Daftar launcher untuk galeri
//        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK && result.data != null) {
//                selectedImageUri = result.data!!.data
//                if (selectedImageUri != null) {
//                    openCropIntent(selectedImageUri!!)
//                }
//            }
//        }
//
//        // Daftar launcher untuk cropping
//        cropLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == RESULT_OK && result.data != null) {
//                val extras = result.data!!.extras
//                val croppedBitmap = extras?.getParcelable<Bitmap>("data")
//                if (croppedBitmap != null) {
//                    mBitmap = scaleImage(croppedBitmap)
//                    binding.mPhotoImageView.setImageBitmap(mBitmap)
//                } else {
//                    Toast.makeText(this, "Gagal memuat gambar yang di-crop.", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }

        binding.mGalleryButton.setOnClickListener { v ->
            // Open gallery to pick an image
            val intent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, pickImageRequest)
        }
        // Tombol untuk membuka galeri
//        binding.mGalleryButton.setOnClickListener {
//            val callGalleryIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
//                type = "image/*"
//            }
//            galleryLauncher.launch(callGalleryIntent)
//        }

        // Tombol untuk mendeteksi penyakit dari gambar
        binding.mDetectButton.setOnClickListener {
            classifyImage()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == pickImageRequest && resultCode == RESULT_OK && data != null) {
            sourceUri = data.data // Get the selected image URI

            // Set destination URI for the cropped image
            val croppedImageFile = File(cacheDir, "cropped")
            destinationUri = Uri.fromFile(croppedImageFile)

            // Start cropping
            Crop.of(sourceUri, destinationUri).asSquare().start(this)
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                // Ensure the cropped image URI is not null
                val croppedUri = Crop.getOutput(data)
                if (croppedUri != null) {
                    // Decode the cropped image URI into a Bitmap
                    val inputStream: InputStream? = contentResolver.openInputStream(croppedUri)
                    val bitmapImage = BitmapFactory.decodeStream(inputStream)

                    if (bitmapImage != null) {
                        mBitmap = scaleImage(bitmapImage) // Scale the image if needed
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
//
//    // Fungsi untuk membuka intent crop
//    private fun openCropIntent(uri: Uri) {
//        val cropIntent = Intent("com.android.camera.action.CROP").apply {
//            setDataAndType(uri, "image/*")
//            putExtra("crop", "true")
//            putExtra("aspectX", 1)
//            putExtra("aspectY", 1)
//            putExtra("outputX", mInputSize)
//            putExtra("outputY", mInputSize)
//            putExtra("return-data", true)
//        }
//
//        if (cropIntent.resolveActivity(packageManager) != null) {
//            cropLauncher.launch(cropIntent)
//        } else {
//            Toast.makeText(this, "Fitur crop tidak tersedia, gambar langsung ditampilkan.", Toast.LENGTH_SHORT).show()
//            try {
//                mBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
//                binding.mPhotoImageView.setImageBitmap(mBitmap)
//            } catch (e: IOException) {
//                e.printStackTrace()
//                Toast.makeText(this, "Gagal memuat gambar.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


    // Fungsi untuk menyesuaikan ukuran gambar
    private fun scaleImage(bitmap: Bitmap): Bitmap {
        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        val scaleWidth = mInputSize.toFloat() / originalWidth
        val scaleHeight = mInputSize.toFloat() / originalHeight
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(bitmap, 0, 0, originalWidth, originalHeight, matrix, true)
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
