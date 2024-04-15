package com.dannyou.artbooktesting.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "arts")
data class Art(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String,
    val artist: String,
    var year: Int,
    var imageUrl: String?,
)