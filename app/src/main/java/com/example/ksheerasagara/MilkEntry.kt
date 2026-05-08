package com.example.ksheerasagara

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milk_entries")
data class MilkEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val cowName: String,
    val morningLiters: Double,
    val morningFat: Double,
    val morningAmount: Double,
    val eveningLiters: Double,
    val eveningFat: Double,
    val eveningAmount: Double,
    val totalAmount: Double
)