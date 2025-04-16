package com.tflite.DeteksipenyakittanamanMn

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity

class Tentang : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tentang)

        val tvLink = findViewById<TextView>(R.id.tvTelegramLink)
        tvLink.text = Html.fromHtml("<a href='https://t.me/islahul_anwar'>Islahul Anwar</a>")
        tvLink.movementMethod = LinkMovementMethod.getInstance()

    }
}
