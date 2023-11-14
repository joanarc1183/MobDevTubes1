package com.example.tubes1.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tubes1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageFragment = ImageFragment()
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, imageFragment)
            .commit()
    }
}