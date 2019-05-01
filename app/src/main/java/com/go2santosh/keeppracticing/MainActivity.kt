package com.go2santosh.keeppracticing

import kotlinx.android.synthetic.main.activity_main.*
import android.app.Activity
import android.os.Bundle

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textViewCaption.text = getString(R.string.keep_practicing)
    }
}
