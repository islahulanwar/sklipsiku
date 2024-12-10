package com.tflite.DeteksipenyakittanamanMn

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.tflite.DeteksipenyakittanamanMn.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private  lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //memperkenalkan button yang sudah ditambahkan di layout activity_main.xml
//        val btnPendeteksi: Button = findViewById(R.id.btn_move_detection)
//        btnPendeteksi.setOnClickListener(this)
        //menambahkan event onClick pada button btnMoveDetection
        binding.btnMoveDetection.setOnClickListener(this)
//
//        val btnImporGaleri: Button = findViewById(R.id.btn_move_import)
//        btnImporGaleri.setOnClickListener(this)
        binding.btnMoveImport.setOnClickListener(this)

//        val btnBantuan: Button = findViewById(R.id.btn_move_help)
//        btnBantuan.setOnClickListener(this)
            binding.btnMoveHelp.setOnClickListener(this)

//        val btnTentang: Button = findViewById(R.id.btn_move_about)
//        btnTentang.setOnClickListener(this)
        binding.btnMoveHelp.setOnClickListener(this)
    }
    override fun onClick(v: View) {
        when (v.id) {
//            R.id.btn_move_detection
            binding.btnMoveDetection.id -> {
                //menambahkan suatu Intent pada method onClick()
                val moveIntent = Intent(this@MainActivity, Pendeteksi::class.java)
                startActivity(moveIntent)
            }
//            R.id.btn_move_import
            binding.btnMoveImport.id-> {
                val moveIntent = Intent(this@MainActivity, DeteksiDariGaleri::class.java)
                startActivity(moveIntent)
            }
            //R.id.btn_move_help
            binding.btnMoveHelp.id -> {
                val moveIntent = Intent(this@MainActivity, Bantuan::class.java)
                startActivity(moveIntent)
            }
//            R.id.btn_move_about
            binding.btnMoveAbout.id -> {
                val moveIntent = Intent(this@MainActivity, Tentang::class.java)
                startActivity(moveIntent)
            }
        }
    }
}

