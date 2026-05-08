package com.example.ksheerasagara

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.drawerLayout)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val btnAddMilk = findViewById<LinearLayout>(R.id.btnAddMilk)
        val btnAddExpense = findViewById<LinearLayout>(R.id.btnAddExpense)
        val btnCowAnalysis = findViewById<LinearLayout>(R.id.btnCowAnalysis)
        val btnPdfExport = findViewById<LinearLayout>(R.id.btnPdfExport)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        tvDate.text = dateFormat.format(Date())

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        btnAddMilk.setOnClickListener {
            startActivity(Intent(this, AddMilkActivity::class.java))
        }

        btnAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
        btnCowAnalysis.setOnClickListener {
            startActivity(Intent(this, CowAnalysisActivity::class.java))
        }

        btnPdfExport.setOnClickListener {
            startActivity(Intent(this, PdfExportActivity::class.java))
        }

        navigationView.setNavigationItemSelectedListener { item ->
            drawerLayout.closeDrawer(GravityCompat.START)
            when (item.itemId) {
                R.id.nav_history -> startActivity(Intent(this, HistoryActivity::class.java))
                R.id.nav_cow -> startActivity(Intent(this, CowAnalysisActivity::class.java))
                R.id.nav_insights -> startActivity(Intent(this, InsightsActivity::class.java))
                R.id.nav_pdf -> startActivity(Intent(this, PdfExportActivity::class.java))
            }
            true
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_home -> true
                R.id.bottom_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.bottom_insights -> {
                    startActivity(Intent(this, InsightsActivity::class.java))
                    true
                }
                R.id.bottom_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }

                else -> false
            }
        }

        loadDashboard()
    }

    override fun onResume() {
        super.onResume()
        loadDashboard()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun loadDashboard() {
        val tvProfit = findViewById<TextView>(R.id.tvProfit)
        val tvProfitStatus = findViewById<TextView>(R.id.tvProfitStatus)
        val tvIncome = findViewById<TextView>(R.id.tvIncome)
        val tvExpense = findViewById<TextView>(R.id.tvExpense)

        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val totalIncome = db.milkDao().getTotalIncome() ?: 0.0
            val totalExpense = db.expenseDao().getTotalExpenses() ?: 0.0
            val profit = totalIncome - totalExpense

            runOnUiThread {
                tvIncome.text = "₹ %.2f".format(totalIncome)
                tvExpense.text = "₹ %.2f".format(totalExpense)
                tvProfit.text = "₹ %.2f".format(profit)

                if (profit >= 0) {
                    tvProfit.setTextColor(getColor(android.R.color.white))
                    tvProfitStatus.text = "● Profitable"
                    tvProfitStatus.setTextColor(getColor(android.R.color.holo_green_light))
                } else {
                    tvProfit.setTextColor(getColor(android.R.color.holo_red_light))
                    tvProfitStatus.text = "● Loss"
                    tvProfitStatus.setTextColor(getColor(android.R.color.holo_red_light))
                }
            }
        }
    }
}