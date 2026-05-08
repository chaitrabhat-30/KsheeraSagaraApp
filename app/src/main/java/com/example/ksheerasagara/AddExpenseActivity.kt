package com.example.ksheerasagara

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)

        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val etDescription = findViewById<EditText>(R.id.etExpenseDescription)
        val etAmount = findViewById<EditText>(R.id.etExpenseAmount)
        val etDate = findViewById<EditText>(R.id.etExpenseDate)
        val btnSave = findViewById<Button>(R.id.btnSaveExpense)
        val btnBack = findViewById<Button>(R.id.btnBackExpense)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        etDate.setText(dateFormat.format(Date()))

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val category = spinnerCategory.selectedItem.toString()
            val description = etDescription.text.toString()
            val amount = etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val date = etDate.text.toString()

            if (description.isEmpty()) {
                Toast.makeText(this, "Please enter description!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amount == 0.0) {
                Toast.makeText(this, "Please enter amount!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = ExpenseEntry(
                date = date,
                category = category,
                description = description,
                amount = amount
            )

            val db = AppDatabase.getDatabase(this)
            lifecycleScope.launch {
                db.expenseDao().insertExpense(expense)
                runOnUiThread {
                    Toast.makeText(this@AddExpenseActivity,
                        "Expense saved! ₹$amount", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }
}