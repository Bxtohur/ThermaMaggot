package com.bitohur.thermamaggot.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bitohur.thermamaggot.FloorActivity
import com.bitohur.thermamaggot.R
import com.bitohur.thermamaggot.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var database: DatabaseReference

    private val temperatureList = mutableListOf<String>()
    private val humidityList = mutableListOf<String>()
    private val floorListTherma = mutableListOf<String>()

    private val fanList = mutableListOf<String>()
    private val lampList = mutableListOf<String>()
    private val pumpList = mutableListOf<String>()
    private val floorListTools = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance().reference

        fetchSensorData()
        fetchControlSystemData()

        binding.manualBtn.setOnClickListener {
            updateKontrolOtomatis(false)
            val intent = Intent(requireContext(), FloorActivity::class.java)
            startActivity(intent)
        }

        binding.autoBtn.setOnClickListener {
            updateKontrolOtomatis(true)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewPagerTherma()

        setupViewPagerTools()
    }

    private fun setupViewPagerTherma() {
        val viewPager2Therma = binding.viewPager
        viewPager2Therma.adapter = ViewPagerTehrmaAdapter(temperatureList, humidityList, floorListTherma)
        viewPager2Therma.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.dotIndicator.setViewPager(viewPager2Therma)

        viewPager2Therma.offscreenPageLimit = 3
    }

    private fun setupViewPagerTools() {
        val viewPager2Tools = binding.viewPagerTools
        viewPager2Tools.adapter = ViewPagerToolsAdapter(fanList, lampList, pumpList, floorListTools)
        viewPager2Tools.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.dotIndicatorTools.setViewPager(viewPager2Tools)

        viewPager2Tools.offscreenPageLimit = 3
    }

    private fun fetchSensorData() {
        val sensorRef = database.child("sensor")

        sensorRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                temperatureList.clear()
                humidityList.clear()
                floorListTherma.clear()

                for (floorSnapshot in snapshot.children) {
                    val floor = floorSnapshot.key.orEmpty()
                    val temperature = floorSnapshot.child("suhu").getValue(Double::class.java) ?: 0.0
                    val humidity = floorSnapshot.child("kelembapan").getValue(Double::class.java) ?: 0.0

                    // Add data to the lists
                    addToListTherma("${temperature}\u00B0C", "${humidity}%",
                        formatFloor(floor).replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.getDefault()
                            ) else it.toString()
                        })
                }

                Log.d("HomeFragment", "Temperature: $temperatureList, Humidity: $humidityList, Floor: $floorListTherma")

                // Notify adapter of data change
                binding.viewPager.adapter?.notifyDataSetChanged()
                binding.dotIndicator.setViewPager(binding.viewPager)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchControlSystemData() {
        val controlRef = database.child("kontrolsistem")

        controlRef.addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                fanList.clear()
                lampList.clear()
                pumpList.clear()
                floorListTools.clear()

                var kontrolOtomatis: Boolean? = null

                for (floorSnapshot in snapshot.children) {
                    val floorKey = floorSnapshot.key.orEmpty()
                    if (floorKey.startsWith("lantai")) {
                        val fan = if (floorSnapshot.child("kipas${floorKey.last()}").getValue(Boolean::class.java) == true) "ON" else "OFF"
                        val lamp = if (floorSnapshot.child("lampu${floorKey.last()}").getValue(Boolean::class.java) == true) "ON" else "OFF"
                        val pump = if (floorSnapshot.child("pompa${floorKey.last()}").getValue(Boolean::class.java) == true) "ON" else "OFF"

                        addToListTools(fan, lamp, pump,
                            formatFloor(floorKey).replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            })
                    }
                    kontrolOtomatis = snapshot.child("kontrolotomatis").getValue(Boolean::class.java)
                }

                Log.d("HomeFragment", "Fan: $fanList, Lamp: $lampList, Pump: $pumpList, Floor: $floorListTools")

                updateButtonColors(kontrolOtomatis ?: false)

                binding.viewPagerTools.adapter?.notifyDataSetChanged()
                binding.dotIndicatorTools.setViewPager(binding.viewPagerTools)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateKontrolOtomatis(value: Boolean) {
        val controlRef = database.child("kontrolsistem")
        controlRef.child("kontrolotomatis").setValue(value)
            .addOnSuccessListener {
                Log.d("HomeFragment", "kontrolotomatis updated successfully to $value")
            }
            .addOnFailureListener { error ->
                Log.e("HomeFragment", "Failed to update kontrolotomatis: ${error.message}")
                Toast.makeText(requireContext(), "Failed to update kontrolotomatis", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addToListTherma(temperature: String, humidity: String, floor: String) {
        temperatureList.add(temperature)
        humidityList.add(humidity)
        floorListTherma.add(floor)
    }

    private fun addToListTools(fan: String, lamp: String, pump: String, floor: String) {
        fanList.add(fan)
        lampList.add(lamp)
        pumpList.add(pump)
        floorListTools.add(floor)
    }

    private fun updateButtonColors(kontrolOtomatis: Boolean) {
        val (inactiveDrawable, activeDrawable) = R.drawable.rounded_background to R.drawable.rounded_background_active

        binding.manualBtn.background = ContextCompat.getDrawable(requireContext(), if (kontrolOtomatis) inactiveDrawable else activeDrawable)
        binding.autoBtn.background = ContextCompat.getDrawable(requireContext(), if (kontrolOtomatis) activeDrawable else inactiveDrawable)
    }

    private fun formatFloor(floor: String): String {
        return floor.replaceFirst(Regex("(\\D+)(\\d+)"), "$1 $2")
    }
}
