package com.bitohur.thermamaggot.data

data class HistoryItem(
    val date: String = "",
    val lantai1: FloorData = FloorData(),
    val lantai2: FloorData = FloorData(),
    val lantai3: FloorData = FloorData()
)
