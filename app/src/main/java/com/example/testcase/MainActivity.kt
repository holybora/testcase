package com.example.testcase

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {

    //TODO: test me please
    private val textView by lazy { TextView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState,)
        setContentView(textView)
        textView.setTextColor(Color.WHITE)
        val body = getBody(
            applicationContext.storage().getLastTimestamp(),
            applicationContext.storage().getDelta()
        )
        textView.text = body
    }

}