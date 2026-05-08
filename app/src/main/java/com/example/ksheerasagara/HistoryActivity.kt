package com.example.ksheerasagara

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val recycler = findViewById<RecyclerView>(R.id.recyclerHistory)
        val btnBack = findViewById<Button>(R.id.btnBackHistory)

        recycler.layoutManager = LinearLayoutManager(this)
        btnBack.setOnClickListener { finish() }

        loadHistory(recycler)
    }

    private fun loadHistory(recycler: RecyclerView) {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val milkEntries = db.milkDao().getAllMilkEntries()
            runOnUiThread {
                recycler.adapter = MilkHistoryAdapter(milkEntries) { entry ->
                    lifecycleScope.launch {
                        db.milkDao().deleteMilkEntry(entry)
                        runOnUiThread {
                            Toast.makeText(
                                this@HistoryActivity,
                                "Entry deleted!",
                                Toast.LENGTH_SHORT
                            ).show()
                            loadHistory(recycler)
                        }
                    }
                }
            }
        }
    }
}