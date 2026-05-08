package com.example.ksheerasagara

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddMilkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_milk)

        val etMorningLiters = findViewById<EditText>(R.id.etMorningLiters)
        val etMorningFat = findViewById<EditText>(R.id.etMorningFat)
        val etMorningAmount = findViewById<EditText>(R.id.etMorningAmount)
        val etEveningLiters = findViewById<EditText>(R.id.etEveningLiters)
        val etEveningFat = findViewById<EditText>(R.id.etEveningFat)
        val etEveningAmount = findViewById<EditText>(R.id.etEveningAmount)
        val etCowName = findViewById<EditText>(R.id.etCowName)
        val btnSave = findViewById<Button>(R.id.btnSaveMilk)
        val btnBack = findViewById<Button>(R.id.btnBackMilk)

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val cowName = etCowName.text.toString()
            val morningLiters = etMorningLiters.text.toString().toDoubleOrNull() ?: 0.0
            val morningFat = etMorningFat.text.toString().toDoubleOrNull() ?: 0.0
            val morningAmount = etMorningAmount.text.toString().toDoubleOrNull() ?: 0.0
            val eveningLiters = etEveningLiters.text.toString().toDoubleOrNull() ?: 0.0
            val eveningFat = etEveningFat.text.toString().toDoubleOrNull() ?: 0.0
            val eveningAmount = etEveningAmount.text.toString().toDoubleOrNull() ?: 0.0
            val totalAmount = morningAmount + eveningAmount

            if (cowName.isEmpty()) {
                Toast.makeText(this, "Please enter cow name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (morningLiters == 0.0 && eveningLiters == 0.0) {
                Toast.makeText(this, "Please enter liters!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (morningAmount == 0.0 && eveningAmount == 0.0) {
                Toast.makeText(this, "Please enter amount received!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val today = dateFormat.format(Date())

            val entry = MilkEntry(
                date = today,
                cowName = cowName,
                morningLiters = morningLiters,
                morningFat = morningFat,
                morningAmount = morningAmount,
                eveningLiters = eveningLiters,
                eveningFat = eveningFat,
                eveningAmount = eveningAmount,
                totalAmount = totalAmount
            )

            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                db.milkDao().insertMilkEntry(entry)
                runOnUiThread {
                    Toast.makeText(this@AddMilkActivity,
                        "Milk entry saved! ₹$totalAmount", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}