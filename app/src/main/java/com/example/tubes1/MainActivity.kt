package com.example.tubes1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tubes1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainFragment = MainFragment()
        supportFragmentManager.beginTransaction().replace(binding.fragmentContainer.id, mainFragment)
    }
}