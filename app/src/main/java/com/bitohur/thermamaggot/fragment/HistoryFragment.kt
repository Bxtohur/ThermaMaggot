// HistoryFragment.kt
package com.bitohur.thermamaggot.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitohur.thermamaggot.data.FloorData
import com.bitohur.thermamaggot.data.HistoryItem
import com.bitohur.thermamaggot.databinding.FragmentHistoryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private val historyList = mutableListOf<HistoryItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val adapter = HistoryAdapter(historyList)
        recyclerView.adapter = adapter

        fetchDataFromFirebase()
    }

    private fun fetchDataFromFirebase() {
        val database = FirebaseDatabase.getInstance().reference.child("histori")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                historyList.clear()
                for (dateSnapshot in snapshot.children) {
                    val date = dateSnapshot.key ?: continue
                    val lantai1 = dateSnapshot.child("lantai1").getValue(FloorData::class.java)
                    val lantai2 = dateSnapshot.child("lantai2").getValue(FloorData::class.java)
                    val lantai3 = dateSnapshot.child("lantai3").getValue(FloorData::class.java)

                    if (lantai1 != null && lantai2 != null && lantai3 != null) {
                        historyList.add(
                            HistoryItem(date, lantai1, lantai2, lantai3)
                        )
                    }
                }
                (binding.recyclerView.adapter as HistoryAdapter).notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@HistoryFragment.context, "$error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
