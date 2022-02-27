package com.artem_obrazumov.oral_rus_trainer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var icon: ImageView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        icon = findViewById(R.id.icon)
        title = findViewById(R.id.title)

        icon.animate().alpha(1f).translationY(0f).setDuration(1000L)
        title.animate().alpha(1f).translationY(0f).setDuration(1000L)

        Handler(Looper.getMainLooper()).postDelayed({
            onAnimationFinished()
        }, 3000L)
    }

    private fun onAnimationFinished() {
        val intent = Intent(this, MainActivity::class.java)
        finish()
        startActivity(intent)
    }
}