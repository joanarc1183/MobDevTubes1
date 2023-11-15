package com.example.tubes1.view

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.tubes1.R
import com.example.tubes1.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val drawerLayout:DrawerLayout = findViewById(R.id.drawer_layout)
//        val navview: NavigationView = findViewById(R.id.navView)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.id.date, R.id.name)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val imageFragment = ImageFragment()
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, imageFragment)
            .commit()

        val switch: Switch = findViewById(R.id.switcher)
        var currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        switch.setOnCheckedChangeListener { _, isChecked ->
            val newNightMode = if (isChecked) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
            if (currentNightMode != newNightMode) {
                // Update the current night mode
                currentNightMode = newNightMode

                // Apply the new night mode
                AppCompatDelegate.setDefaultNightMode(newNightMode)
                delegate.applyDayNight()
                setTheme(if (isChecked) R.style.AppTheme_Light else R.style.AppTheme_Dark)

            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}