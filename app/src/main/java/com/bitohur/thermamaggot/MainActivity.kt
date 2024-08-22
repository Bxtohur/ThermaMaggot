package com.bitohur.thermamaggot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import me.relex.circleindicator.CircleIndicator3

class MainActivity : AppCompatActivity() {

    private var temperatureList = mutableListOf<String>()
    private var humidityList = mutableListOf<String>()
    private var floorListTherma = mutableListOf<String>()

    private var fanList = mutableListOf<String>()
    private var lampList = mutableListOf<String>()
    private var pumpList = mutableListOf<String>()
    private var floorListTools = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewPager2Therma = findViewById<ViewPager2>(R.id.view_pager)
        val viewPager2Tools = findViewById<ViewPager2>(R.id.view_pager_tools)

        postToListTherma()
        postToListTools()

        viewPager2Therma.adapter = ViewPagerTehrmaAdapter(temperatureList, humidityList, floorListTherma)
        viewPager2Therma.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        viewPager2Tools.adapter = ViewPagerToolsAdapter(fanList, lampList, pumpList, floorListTools)
        viewPager2Tools.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicator = findViewById<CircleIndicator3>(R.id.dot_indicator)
        indicator.setViewPager(viewPager2Therma)

        val indicatorTools = findViewById<CircleIndicator3>(R.id.dot_indicator_tools)
        indicatorTools.setViewPager(viewPager2Tools)
    }

    private fun addToListTherma(temperature: String, humidity: String, floor: String) {
        temperatureList.add(temperature)
        humidityList.add(humidity)
        floorListTherma.add(floor)
    }

    private fun addToListTools(fan: String, lamp: String, pump: String, floor: String) {
        lampList.add(lamp)
        fanList.add(fan)
        pumpList.add(pump)
        floorListTools.add(floor)
    }

    private fun postToListTherma() {
        for (i in 1..3) {
            addToListTherma((10 + i).toString()+"C", (50 + i).toString()+"%", "Lantai $i")
        }
    }

    private fun postToListTools() {
        for (i in 1..3) {
            addToListTools("On","OFF", "ON", "Lantai $i")
        }
    }
}