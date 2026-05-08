package com.example.ksheerasagara

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sharedPrefs = getSharedPreferences("KsheeraSagara", MODE_PRIVATE)

        val tvProfileName = findViewById<TextView>(R.id.tvProfileName)
        val tvProfileCows = findViewById<TextView>(R.id.tvProfileCows)
        val tvProfileLiters = findViewById<TextView>(R.id.tvProfileLiters)
        val tvProfileEntries = findViewById<TextView>(R.id.tvProfileEntries)
        val btnLogout = findViewById<Button>(R.id.btnLogout)
        val btnBack = findViewById<ImageButton>(R.id.btnBackProfile)

        val farmerName = sharedPrefs.getString("farmerName", "Farmer") ?: "Farmer"
        tvProfileName.text = farmerName

        btnBack.setOnClickListener { finish() }

        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val milkEntries = db.milkDao().getAllMilkEntries()
            val totalCows = milkEntries.map { it.cowName }.distinct().size
            val totalLiters = milkEntries.sumOf { it.morningLiters + it.eveningLiters }

            runOnUiThread {
                tvProfileCows.text = "$totalCows"
                tvProfileLiters.text = "%.1fL".format(totalLiters)
                tvProfileEntries.text = "${milkEntries.size}"
            }
        }

        btnLogout.setOnClickListener {
            sharedPrefs.edit()
                .putBoolean("isLoggedIn", false)
                .remove("farmerName")
                .apply()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}