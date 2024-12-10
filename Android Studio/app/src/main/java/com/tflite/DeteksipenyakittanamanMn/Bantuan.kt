package com.tflite.DeteksipenyakittanamanMn

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import com.tflite.DeteksipenyakittanamanMn.databinding.ActivityBantuanBinding

class Bantuan : AppCompatActivity() {
    private lateinit var binding: ActivityBantuanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBantuanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "Halaman Bantuan"
        }
    }
}
