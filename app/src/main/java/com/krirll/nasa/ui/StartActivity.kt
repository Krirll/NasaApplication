package com.krirll.nasa.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.TextView
import com.krirll.nasa.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        startAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 2000
        )
    }
    private fun startAnimation() {
        val image = findViewById<ImageView>(R.id.startImageView)
        val text = findViewById<TextView>(R.id.startTextView)
        AlphaAnimation(0.1f, 1.0f).apply {
            duration = 1500
            image.startAnimation(this)
            text.startAnimation(this)
        }
    }
}