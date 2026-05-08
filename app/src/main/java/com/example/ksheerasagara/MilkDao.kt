package com.example.ksheerasagara

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface MilkDao {
    @Insert
    suspend fun insertMilkEntry(entry: MilkEntry)

    @Query("SELECT * FROM milk_entries ORDER BY id DESC")
    suspend fun getAllMilkEntries(): List<MilkEntry>

    @Query("SELECT SUM(totalAmount) FROM milk_entries")
    suspend fun getTotalIncome(): Double?
    @Delete
    suspend fun deleteMilkEntry(entry: MilkEntry)

    @Query("DELETE FROM milk_entries")
    suspend fun deleteAllMilkEntries()
}