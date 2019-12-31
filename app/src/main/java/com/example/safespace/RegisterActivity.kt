package com.example.safespace

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity: AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        //Hide Status Bar
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_register)
    }
}