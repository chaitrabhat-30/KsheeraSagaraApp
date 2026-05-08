package com.example.ksheerasagara

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: ExpenseEntry)

    @Query("SELECT * FROM expense_entries ORDER BY id DESC")
    suspend fun getAllExpenses(): List<ExpenseEntry>

    @Query("SELECT SUM(amount) FROM expense_entries")
    suspend fun getTotalExpenses(): Double?
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntry)

    @Query("DELETE FROM expense_entries")
    suspend fun deleteAllExpenses()
}