package com.example.ksheerasagara

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MilkHistoryAdapter(
    private val entries: List<MilkEntry>,
    private val onDelete: (MilkEntry) -> Unit
) : RecyclerView.Adapter<MilkHistoryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView = view.findViewById(R.id.tvItemDate)
        val tvCow: TextView = view.findViewById(R.id.tvItemCow)
        val tvAmount: TextView = view.findViewById(R.id.tvItemAmount)
        val tvLiters: TextView = view.findViewById(R.id.tvItemLiters)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_milk_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.tvDate.text = entry.date
        holder.tvCow.text = "Cow: ${entry.cowName}"
        holder.tvAmount.text = "Rs.${entry.totalAmount}"
        holder.tvLiters.text = "${entry.morningLiters + entry.eveningLiters}L"
        holder.btnDelete.setOnClickListener { onDelete(entry) }
    }

    override fun getItemCount() = entries.size
}