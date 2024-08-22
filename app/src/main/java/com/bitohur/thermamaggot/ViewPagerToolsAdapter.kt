package com.bitohur.thermamaggot

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bitohur.thermamaggot.databinding.ItemToolsCardBinding

class ViewPagerToolsAdapter(
    private var fan: List<String>,
    private var lamp: List<String>,
    private var pump: List<String>,
    private var floor: List<String>
) : RecyclerView.Adapter<ViewPagerToolsAdapter.Pager2ViewHolder>() {

    inner class Pager2ViewHolder(val binding: ItemToolsCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Pager2ViewHolder {
        val binding = ItemToolsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Pager2ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerToolsAdapter.Pager2ViewHolder, position: Int) {
        holder.binding.tvValueFan.text = fan[position]
        holder.binding.tvValueLamp.text = lamp[position]
        holder.binding.tvValuePump.text = pump[position]
        holder.binding.tvFloor.text = floor[position]
    }

    override fun getItemCount(): Int {
        return fan.size
    }
}
