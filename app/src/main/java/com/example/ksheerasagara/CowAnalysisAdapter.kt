package com.example.ksheerasagara

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CowAnalysisAdapter(private val cows: List<CowSummary>) :
    RecyclerView.Adapter<CowAnalysisAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCowName: TextView = view.findViewById(R.id.tvItemCow)
        val tvIncome: TextView = view.findViewById(R.id.tvItemAmount)
        val tvLiters: TextView = view.findViewById(R.id.tvItemLiters)
        val tvDate: TextView = view.findViewById(R.id.tvItemDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_milk_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cow = cows[position]
        holder.tvCowName.text = "🐄 ${cow.cowName}"
        holder.tvIncome.text = "₹${cow.totalIncome}"
        holder.tvLiters.text = "%.1fL total".format(cow.totalLiters)
        holder.tvDate.text = "Tap to see details"
    }

    override fun getItemCount() = cows.size
}