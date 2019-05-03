package com.go2santosh.keeppracticing

import kotlinx.android.synthetic.main.activity_splash.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils

class SplashActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        textViewSplash.text = getString(R.string.keep_practicing)
        animateLaunchLogo()
    }

    private fun animateLaunchLogo() {

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.anim_splash_text)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                startMainActivity()
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        textViewSplash.startAnimation(animation)
    }

    private fun startMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
