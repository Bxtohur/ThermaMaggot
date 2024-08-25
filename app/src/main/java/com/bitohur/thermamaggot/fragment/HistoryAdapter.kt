package com.bitohur.thermamaggot.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitohur.thermamaggot.data.HistoryItem
import com.bitohur.thermamaggot.databinding.ItemHistoryBinding

class HistoryAdapter(private val historyList: List<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(private val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(historyItem: HistoryItem) {
            binding.tvDate.text = historyItem.date
            binding.tvFloor.text = "Lantai 1"
            binding.tvTemperature.text = "Suhu"
            binding.tvValueTemperature.text = "${historyItem.lantai1.suhu}°C"
            binding.tvHumidity.text = "Kelembaban"
            binding.tvValueHumidity.text = "${historyItem.lantai1.kelembapan}%"

            binding.tvFloor2.text = "Lantai 2"
            binding.tvTemperature2.text = "Suhu"
            binding.tvValueTemperature2.text = "${historyItem.lantai2.suhu}°C"
            binding.tvHumidity2.text = "Kelembaban"
            binding.tvValueHumidity2.text = "${historyItem.lantai2.kelembapan}%"

            binding.tvFloor3.text = "Lantai 3"
            binding.tvTemperature3.text = "Suhu"
            binding.tvValueTemperature3.text = "${historyItem.lantai3.suhu}°C"
            binding.tvHumidity3.text = "Kelembaban"
            binding.tvValueHumidity3.text = "${historyItem.lantai3.kelembapan}%"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = historyList[position]
        holder.bind(historyItem)
    }

    override fun getItemCount(): Int = historyList.size
}
