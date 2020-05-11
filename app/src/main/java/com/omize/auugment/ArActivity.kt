package com.omize.auugment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.content.pm.ActivityInfo
import android.widget.ImageButton

class ArActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar)
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        val bckbtn = findViewById<ImageButton>(R.id.goBackButton)
        bckbtn.setOnClickListener {
            finish()
        }

    }
}
