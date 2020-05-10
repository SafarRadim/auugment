package com.omize.auugment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

const val EXTRA_MESSAGE = "com.omize.auugment.MESSAGE"
const val EXTRA_MESSAGE2 = "com.omize.auugment.MESSAGE2"
var foo_value = 0
var bar_value = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun activityAR(view: View) {
        val text = "Not my job -R"
        val intent = Intent(this, ArActivity::class.java).apply{
            putExtra(EXTRA_MESSAGE, text)
        }
        startActivity(intent)
    }

    fun displayFooBar(view: View){
        val foo_val = foo_value.toString()
        val bar_val = bar_value.toString()
        val intent = Intent(this, DisplayFooBar::class.java).apply{
            putExtra(EXTRA_MESSAGE, foo_val)
            putExtra(EXTRA_MESSAGE2, bar_val)
        }
        startActivity(intent)
    }

    fun increaseFoo(view: View){
        foo_value++
        displayFooBar(view)
    }

    fun decreaseFoo(view: View){
        foo_value--
        displayFooBar(view)
    }

    fun increaseBar(view: View){
        bar_value++
        displayFooBar(view)
    }

    fun decreaseBar(view: View){
        bar_value--
        displayFooBar(view)
    }
}
