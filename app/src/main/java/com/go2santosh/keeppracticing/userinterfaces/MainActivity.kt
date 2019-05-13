package com.go2santosh.keeppracticing.userinterfaces

import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.go2santosh.keeppracticing.R
import com.go2santosh.keeppracticing.userinterfaces.contents.ContentsActivity

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCaption.text = getString(R.string.keep_practicing)
        buttonExploreContents.apply { setOnClickListener { startContents() } }
    }

    private fun startContents() {
        val intent = Intent(applicationContext, ContentsActivity::class.java)
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}
