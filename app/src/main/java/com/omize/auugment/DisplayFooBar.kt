package com.omize.auugment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class DisplayFooBar : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_foo_bar)

        val foo = intent.getStringExtra(EXTRA_MESSAGE)
        val bar = intent.getStringExtra(EXTRA_MESSAGE2)

        val textViewFoo = findViewById<TextView>(R.id.textViewFooCount).apply {
            text = foo
        }

        val textViewBar = findViewById<TextView>(R.id.textViewBarCount).apply {
            text = bar
        }

    }
}
