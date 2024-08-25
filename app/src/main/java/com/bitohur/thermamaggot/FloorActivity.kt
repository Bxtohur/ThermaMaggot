package com.bitohur.thermamaggot

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bitohur.thermamaggot.databinding.ActivityFloorBinding
import com.google.firebase.database.*

class FloorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFloorBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFloorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance().getReference()

        binding.apply {
            firstFloor.setOnClickListener { fetchAndNavigate(1) }
            secondFloor.setOnClickListener { fetchAndNavigate(2) }
            thirdFloor.setOnClickListener { fetchAndNavigate(3) }
        }
    }

    private fun fetchAndNavigate(floor: Int) {
        database.child("kontrolsistem").get().addOnSuccessListener { kontrolSnapshot ->
            database.child("sensor").get().addOnSuccessListener { sensorSnapshot ->
                val controlData = kontrolSnapshot.child("lantai$floor").value.toString()
                val sensorData = sensorSnapshot.child("lantai$floor").value.toString()

                val intent = Intent(this, ControlingActivity::class.java).apply {
                    putExtra("floor", floor)
                    putExtra("controlData", controlData)
                    putExtra("sensorData", sensorData)
                }
                startActivity(intent)
            }
        }
    }
}
