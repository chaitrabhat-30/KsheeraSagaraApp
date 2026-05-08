package com.example.ksheerasagara

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPrefs = getSharedPreferences("KsheeraSagara", MODE_PRIVATE)

        if (sharedPrefs.getBoolean("isLoggedIn", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_login)

        val etFarmerName = findViewById<EditText>(R.id.etFarmerName)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val name = etFarmerName.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please enter your name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != "admin123") {
                Toast.makeText(this, "Wrong password! Use admin123", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            sharedPrefs.edit()
                .putBoolean("isLoggedIn", true)
                .putString("farmerName", name)
                .apply()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}