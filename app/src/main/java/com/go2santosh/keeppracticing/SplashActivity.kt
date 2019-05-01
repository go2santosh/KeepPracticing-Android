package com.go2santosh.keeppracticing

import kotlinx.android.synthetic.main.activity_splash.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class SplashActivity : Activity() {

    private var delayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000

    internal val startMainActivity: Runnable = Runnable {
        if (!isFinishing) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        textViewSplash.text = getString(R.string.keep_practicing)
        delayHandler = Handler()
        delayHandler!!.postDelayed(startMainActivity, SPLASH_DELAY)
    }

    public override fun onDestroy() {
        if (delayHandler != null) {
            delayHandler!!.removeCallbacks(startMainActivity)
        }
        super.onDestroy()
    }
}
