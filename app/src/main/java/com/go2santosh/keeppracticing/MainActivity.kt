package com.go2santosh.keeppracticing

import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCaption.text = getString(R.string.keep_practicing)
        buttonStart.text = getString(R.string.start)
        buttonStart.run { setOnClickListener { startQuiz() } }
    }

    private fun startQuiz() {
        val intent = Intent(applicationContext, QuizActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
