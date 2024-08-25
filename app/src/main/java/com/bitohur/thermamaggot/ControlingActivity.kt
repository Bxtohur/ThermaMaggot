package com.bitohur.thermamaggot

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitohur.thermamaggot.databinding.ActivityControlingBinding
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject

class ControlingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControlingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControlingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var floor = intent.getIntExtra("floor", 1)
        val controlDataJson = intent.getStringExtra("controlData")
        val sensorDataJson = intent.getStringExtra("sensorData")

        when (floor){
            1-> binding.tvHeaderFloor.text = "KONTROL MANUAL LANTAI PERTAMA"
            2-> binding.tvHeaderFloor.text = "KONTROL MANUAL LANTAI KEDUA"
            3-> binding.tvHeaderFloor.text = "KONTROL MANUAL LANTAI KETIGA"
        }



        sensorDataJson?.let {
            val sensorData = JSONObject(it)
            binding.tvValueTemperature.text = "${sensorData.getDouble("suhu")}°C"
            binding.tvValueHumidity.text = "${sensorData.getDouble("kelembapan")}%"
        }

        // Parse control data (fan, lamp, and pump status)
        controlDataJson?.let {
            val controlData = JSONObject(it)
            updateControlStatus(controlData, floor)
        }

        // Set button click listeners for controlling fan, lamp, and pump
        setupControlButtons(floor)
    }

    private fun updateControlStatus(controlData: JSONObject, floor: Int) {
        val fanStatus = controlData.getBoolean("kipas$floor")
        val lampStatus = controlData.getBoolean("lampu$floor")
        val pumpStatus = controlData.getBoolean("pompa$floor")

        // Update UI based on current statuses
        updateFanUI(fanStatus)
        updateLampUI(lampStatus)
        updatePumpUI(pumpStatus)
    }

    private fun updateFanUI(isOn: Boolean) {
        if (isOn) {
            binding.fanButtonOn.setBackgroundResource(R.drawable.rounded_background_active)
            binding.fanButtonOff.setBackgroundResource(R.drawable.rounded_background)
        } else {
            binding.fanButtonOn.setBackgroundResource(R.drawable.rounded_background)
            binding.fanButtonOff.setBackgroundResource(R.drawable.rounded_background_active)
        }
    }

    private fun updateLampUI(isOn: Boolean) {
        if (isOn) {
            binding.lampButtonOn.setBackgroundResource(R.drawable.rounded_background_active)
            binding.lampButtonOff.setBackgroundResource(R.drawable.rounded_background)
        } else {
            binding.lampButtonOn.setBackgroundResource(R.drawable.rounded_background)
            binding.lampButtonOff.setBackgroundResource(R.drawable.rounded_background_active)
        }
    }

    private fun updatePumpUI(isOn: Boolean) {
        if (isOn) {
            binding.pumpButtonOn.setBackgroundResource(R.drawable.rounded_background_active)
            binding.pumpButtonOff.setBackgroundResource(R.drawable.rounded_background)
        } else {
            binding.pumpButtonOn.setBackgroundResource(R.drawable.rounded_background)
            binding.pumpButtonOff.setBackgroundResource(R.drawable.rounded_background_active)
        }
    }

    private fun setupControlButtons(floor: Int) {
        binding.fanButtonOn.setOnClickListener {
            // Turn on the fan
            updateFanUI(true)
            controlFan(true, floor)
        }

        binding.fanButtonOff.setOnClickListener {
            // Turn off the fan
            updateFanUI(false)
            controlFan(false, floor)
        }

        binding.lampButtonOn.setOnClickListener {
            // Turn on the lamp
            updateLampUI(true)
            controlLamp(true, floor)
        }

        binding.lampButtonOff.setOnClickListener {
            // Turn off the lamp
            updateLampUI(false)
            controlLamp(false, floor)
        }

        binding.pumpButtonOn.setOnClickListener {
            // Turn on the pump
            updatePumpUI(true)
            controlPump(true, floor)
        }

        binding.pumpButtonOff.setOnClickListener {
            // Turn off the pump
            updatePumpUI(false)
            controlPump(false, floor)
        }
    }

    private fun controlFan(isOn: Boolean, floor: Int) {
        // Control fan logic, sending updates to Firebase
        val controlPath = "kontrolsistem/lantai$floor/kipas$floor"
        FirebaseDatabase.getInstance().getReference(controlPath).setValue(isOn)
    }

    private fun controlLamp(isOn: Boolean, floor: Int) {
        // Control lamp logic, sending updates to Firebase
        val controlPath = "kontrolsistem/lantai$floor/lampu$floor"
        FirebaseDatabase.getInstance().getReference(controlPath).setValue(isOn)
    }

    private fun controlPump(isOn: Boolean, floor: Int) {
        // Control pump logic, sending updates to Firebase
        val controlPath = "kontrolsistem/lantai$floor/pompa$floor"
        FirebaseDatabase.getInstance().getReference(controlPath).setValue(isOn)
    }
}
