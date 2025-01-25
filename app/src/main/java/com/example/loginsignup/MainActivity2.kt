package com.example.loginsignup

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.loginsignup.databinding.ActivityMain2Binding
import com.example.loginsignup.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    private val binding: ActivityMain2Binding by lazy {
        ActivityMain2Binding .inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
//        setContentView(R.layout.activity_main2)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnC.setOnClickListener {
            startActivity(Intent(this, Notes::class.java))
        }
        binding.btnV.setOnClickListener {
            startActivity(Intent(this, AllNotes::class.java))
        }

    }
}