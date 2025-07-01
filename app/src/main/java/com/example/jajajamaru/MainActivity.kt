package com.example.jajajamaru

import android.graphics.Point
import android.os.Bundle
import android.os.Handler
import android.view.Display
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.ui.AppBarConfiguration

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration


    val handler = Handler()
    lateinit var custom : MyCustomView
    lateinit var controller : MyCustomView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val display: Display = this.windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        custom = findViewById<MyCustomView>(R.id.mycustom)
        custom.post { custom.beginAnimation() }






        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        mainTsugiNoSyori()
    }


    fun mainTsugiNoSyori() {
        findViewById<TextView>(R.id.textLabelX).text=custom.clickX.toString()
        findViewById<TextView>(R.id.textLabelY).text=custom.clickY.toString()
        findViewById<TextView>(R.id.clickJohoLabel).text=custom.clickState.toString()

        handler.postDelayed( { mainTsugiNoSyori() }, 100)
    }

}