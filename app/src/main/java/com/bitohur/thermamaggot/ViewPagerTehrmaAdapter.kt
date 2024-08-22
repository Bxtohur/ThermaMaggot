package com.bitohur.thermamaggot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitohur.thermamaggot.databinding.ItemThermalCardBinding

class ViewPagerTehrmaAdapter(
    private var temperature: List<String>,
    private var humidity: List<String>,
    private var floor: List<String>
) : RecyclerView.Adapter<ViewPagerTehrmaAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(val binding: ItemThermalCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        val binding = ItemThermalCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Pager2ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: Pager2ViewHolder, position: Int) {
        holder.binding.tvValueTemperature.text = temperature[position]
        holder.binding.tvValueHumidity.text = humidity[position]
        holder.binding.tvFloor.text = floor[position]
    }

    override fun getItemCount(): Int {
        return temperature.size
    }
}
