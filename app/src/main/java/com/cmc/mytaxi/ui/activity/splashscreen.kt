package com.cmc.mytaxi.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.cmc.mytaxi.R

class Splashscreen: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

    Handler().postDelayed({
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    },4000)

    }
}