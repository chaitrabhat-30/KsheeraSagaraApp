package com.example.ksheerasagara

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class PieChartView(context: android.content.Context) : View(context) {
    var categoryMap: Map<String, Double> = emptyMap()
    private val colors = listOf(
        Color.parseColor("#2E7D32"),
        Color.parseColor("#C62828"),
        Color.parseColor("#1565C0"),
        Color.parseColor("#F57F17"),
        Color.parseColor("#6A1B9A")
    )

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (categoryMap.isEmpty()) return
        val total = categoryMap.values.sum().toFloat()
        val cx = width / 2f
        val cy = height / 2f
        val radius = minOf(cx, cy) - 20f
        val rect = RectF(cx - radius, cy - radius, cx + radius, cy + radius)
        val paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
        }
        var startAngle = -90f
        categoryMap.entries.forEachIndexed { index, entry ->
            val sweep = (entry.value / total * 360f).toFloat()
            paint.color = colors[index % colors.size]
            canvas.drawArc(rect, startAngle, sweep, true, paint)
            startAngle += sweep
        }
        paint.color = Color.WHITE
        canvas.drawCircle(cx, cy, radius * 0.5f, paint)
        val textPaint = Paint().apply {
            isAntiAlias = true
            color = Color.parseColor("#1B5E20")
            textSize = 28f
            textAlign = Paint.Align.CENTER
            isFakeBoldText = true
        }
        canvas.drawText("Expenses", cx, cy + 10f, textPaint)
    }
}

class InsightsActivity : AppCompatActivity() {

    private val geminiModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = BuildConfig.GEMINI_API_KEY
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insights)

        val btnBack = findViewById<Button>(R.id.btnBackInsights)
        val btnRefreshAI = findViewById<Button>(R.id.btnRefreshAI)
        val tvAiSuggestion = findViewById<TextView>(R.id.tvAiSuggestion)
        val chartContainer = findViewById<LinearLayout>(R.id.chartContainer)

        btnBack.setOnClickListener { finish() }

        val db = AppDatabase.getDatabase(this)

        fun loadData() {
            lifecycleScope.launch {
                val expenses = db.expenseDao().getAllExpenses()
                val totalIncome = db.milkDao().getTotalIncome() ?: 0.0
                val totalExpense = db.expenseDao().getTotalExpenses() ?: 0.0
                val categoryMap = expenses.groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                runOnUiThread {
                    chartContainer.removeAllViews()
                    if (categoryMap.isNotEmpty()) {
                        val pieView = PieChartView(this@InsightsActivity)
                        pieView.categoryMap = categoryMap
                        val params = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT, 400
                        )
                        pieView.layoutParams = params
                        chartContainer.addView(pieView)

                        val colors = listOf("#2E7D32","#C62828","#1565C0","#F57F17","#6A1B9A")
                        val total = categoryMap.values.sum()
                        categoryMap.entries.forEachIndexed { index, entry ->
                            val percent = (entry.value / total * 100).toInt()
                            val legendRow = LinearLayout(this@InsightsActivity)
                            legendRow.orientation = LinearLayout.HORIZONTAL
                            legendRow.setPadding(16, 8, 16, 8)
                            val colorBox = View(this@InsightsActivity)
                            val boxParams = LinearLayout.LayoutParams(32, 32)
                            boxParams.setMargins(0, 0, 16, 0)
                            colorBox.layoutParams = boxParams
                            colorBox.setBackgroundColor(Color.parseColor(colors[index % colors.size]))
                            val legendText = TextView(this@InsightsActivity)
                            legendText.text = "${entry.key} — Rs.${entry.value.toInt()} ($percent%)"
                            legendText.textSize = 13f
                            legendText.setTextColor(Color.parseColor("#212121"))
                            legendRow.addView(colorBox)
                            legendRow.addView(legendText)
                            chartContainer.addView(legendRow)
                        }
                    } else {
                        val emptyView = TextView(this@InsightsActivity)
                        emptyView.text = "No expense data yet!"
                        emptyView.textSize = 14f
                        emptyView.setPadding(16, 16, 16, 16)
                        chartContainer.addView(emptyView)
                    }
                }

                tvAiSuggestion.text = "Getting AI suggestion..."
                lifecycleScope.launch {
                    try {
                        val profit = totalIncome - totalExpense
                        val prompt = """
                            I am a dairy farmer. My total milk income is Rs.${"%.2f".format(totalIncome)}.
                            My total expenses are Rs.${"%.2f".format(totalExpense)}.
                            My expense breakdown is: ${categoryMap.entries.joinToString { "${it.key}: Rs.${it.value.toInt()}" }}.
                            My net profit is Rs.${"%.2f".format(profit)}.
                            Give me 2-3 short specific cost reduction tips in simple English.
                            Keep response under 100 words.
                        """.trimIndent()

                        val response = geminiModel.generateContent(prompt)
                        runOnUiThread {
                            tvAiSuggestion.text = response.text ?: "No suggestion available"
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            val profit = totalIncome - totalExpense
                            tvAiSuggestion.text = generateFallbackSuggestion(totalIncome, profit, categoryMap)
                        }
                    }
                }
            }
        }

        loadData()
        btnRefreshAI.setOnClickListener { loadData() }
    }

    private fun generateFallbackSuggestion(
        income: Double,
        profit: Double,
        categoryMap: Map<String, Double>
    ): String {
        val fodderCost = categoryMap["Fodder"] ?: 0.0
        val medicalCost = categoryMap["Medical"] ?: 0.0
        return when {
            income == 0.0 -> "Start adding milk entries to get AI suggestions!"
            profit < 0 -> "You are at a loss of Rs.${(-profit).toInt()}! Try reducing fodder costs by switching to home-grown feed!"
            fodderCost > income * 0.5 -> "Fodder cost is more than 50% of income! Growing your own fodder can save Rs.${(fodderCost * 0.3).toInt()}/month!"
            medicalCost > income * 0.2 -> "Medical costs are high! Regular vaccination reduces vet visits and saves money!"
            profit > 0 -> "Great work! Profitable by Rs.${profit.toInt()}! Consider expanding your herd!"
            else -> "Keep adding daily entries for better AI insights!"
        }
    }
}