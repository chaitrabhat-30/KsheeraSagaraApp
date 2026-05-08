package com.example.ksheerasagara

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class PdfExportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_export)

        val btnExport = findViewById<Button>(R.id.btnExportPdf)
        val btnBack = findViewById<Button>(R.id.btnBackPdf)

        btnBack.setOnClickListener { finish() }

        btnExport.setOnClickListener {
            if (checkPermission()) {
                generatePdf()
            } else {
                requestPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            100
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            generatePdf()
        } else {
            Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generatePdf() {
        val db = AppDatabase.getDatabase(this)
        lifecycleScope.launch {
            val milkEntries = db.milkDao().getAllMilkEntries()
            val expenses = db.expenseDao().getAllExpenses()
            val totalIncome = db.milkDao().getTotalIncome() ?: 0.0
            val totalExpense = db.expenseDao().getTotalExpenses() ?: 0.0
            val profit = totalIncome - totalExpense

            runOnUiThread {
                try {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault())
                    val timestamp = dateFormat.format(Date())
                    val fileName = "KsheeraSagara_Report_$timestamp.pdf"

                    val file = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        fileName
                    )

                    val writer = PdfWriter(file)
                    val pdfDocument = PdfDocument(writer)
                    val document = Document(pdfDocument)

                    document.add(Paragraph("KSHEERA-SAGARA MONTHLY REPORT").setFontSize(20f).setBold())
                    document.add(Paragraph("Generated: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}"))
                    document.add(Paragraph("\n"))

                    document.add(Paragraph("PROFIT SUMMARY").setFontSize(16f).setBold())
                    document.add(Paragraph("Total Income: Rs.${"%.2f".format(totalIncome)}"))
                    document.add(Paragraph("Total Expense: Rs.${"%.2f".format(totalExpense)}"))
                    document.add(Paragraph("Net Profit: Rs.${"%.2f".format(profit)}"))
                    document.add(Paragraph("\n"))

                    document.add(Paragraph("MILK ENTRIES").setFontSize(16f).setBold())
                    val milkTable = Table(4)
                    milkTable.addCell("Date")
                    milkTable.addCell("Cow")
                    milkTable.addCell("Liters")
                    milkTable.addCell("Amount")

                    milkEntries.forEach { entry ->
                        milkTable.addCell(entry.date)
                        milkTable.addCell(entry.cowName)
                        milkTable.addCell("${entry.morningLiters + entry.eveningLiters}L")
                        milkTable.addCell("Rs.${entry.totalAmount}")
                    }
                    document.add(milkTable)
                    document.add(Paragraph("\n"))

                    document.add(Paragraph("EXPENSES").setFontSize(16f).setBold())
                    val expenseTable = Table(4)
                    expenseTable.addCell("Date")
                    expenseTable.addCell("Category")
                    expenseTable.addCell("Description")
                    expenseTable.addCell("Amount")

                    expenses.forEach { expense ->
                        expenseTable.addCell(expense.date)
                        expenseTable.addCell(expense.category)
                        expenseTable.addCell(expense.description)
                        expenseTable.addCell("Rs.${expense.amount}")
                    }
                    document.add(expenseTable)

                    document.close()

                    Toast.makeText(
                        this@PdfExportActivity,
                        "PDF saved to Downloads: $fileName",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@PdfExportActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}