package com.example.ksheerasagara

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class CowAnalysisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cow_analysis)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCows)
        val btnBack = findViewById<Button>(R.id.btnBackCow)
        val btnAddCow = findViewById<Button>(R.id.btnAddCow)

        recycler.layoutManager = LinearLayoutManager(this)
        btnBack.setOnClickListener { finish() }

        btnAddCow.setOnClickListener {
            startActivity(android.content.Intent(this, AddMilkActivity::class.java))
        }

        loadCowData()
    }

    private fun loadCowData() {
        val recycler = findViewById<RecyclerView>(R.id.recyclerCows)
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val milkEntries = db.milkDao().getAllMilkEntries()
            val cowMap = milkEntries.groupBy { it.cowName }
            val cowList = cowMap.map { (cowName, entries) ->
                val totalIncome = entries.sumOf { it.totalAmount }
                CowSummary(cowName, totalIncome, entries.sumOf { it.morningLiters + it.eveningLiters })
            }
            runOnUiThread {
                recycler.adapter = CowAnalysisAdapter(cowList)
            }
        }
    }
}