package com.gl4.pizza

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val imageView = findViewById<ImageView>(R.id.imageView)

        // Create a RotateAnimation
        val rotateAnimation = RotateAnimation(
            0f, 360f, // Start and end angles (in degrees)
            Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point (x-axis)
            Animation.RELATIVE_TO_SELF, 0.5f // Pivot point (y-axis)
        )

        // Set animation properties
        rotateAnimation.duration = 5000 // Duration of the animation in milliseconds (3 seconds)
        rotateAnimation.repeatCount = Animation.INFINITE // Infinite rotation
        rotateAnimation.interpolator = LinearInterpolator() // Linear interpolation for smooth rotation

        // Start the animation
        imageView.startAnimation(rotateAnimation)
    }
}