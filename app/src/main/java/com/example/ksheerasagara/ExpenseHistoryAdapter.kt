package com.example.ksheerasagara

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseHistoryAdapter(private val entries: List<ExpenseEntry>) :
    RecyclerView.Adapter<ExpenseHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvItemDate)
        val tvCategory: TextView = view.findViewById(R.id.tvItemCow)
        val tvAmount: TextView = view.findViewById(R.id.tvItemAmount)
        val tvDesc: TextView = view.findViewById(R.id.tvItemLiters)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_milk_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.tvDate.text = entry.date
        holder.tvCategory.text = "💸 ${entry.category}"
        holder.tvAmount.text = "₹${entry.amount}"
        holder.tvDesc.text = entry.description
    }

    override fun getItemCount() = entries.size
}