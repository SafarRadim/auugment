package com.omize.auugment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.R.id.edit
import android.content.Context
import android.text.method.TextKeyListener.clear
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast


class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        findViewById<Button>(R.id.deleteall).setOnClickListener {
            getSharedPreferences("inv", Context.MODE_PRIVATE).edit().clear().commit()
            Toast.makeText(this, "Data byla odebrána", Toast.LENGTH_SHORT).show()
        }
    }
}
