package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "generated_projects")
data class GeneratedProject(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val category: String,
    val description: String,
    val technologies: String, // comma-separated values, e.g. "React, Python, TensorFlow"
    val duration: String, // e.g. "Est. 3 Weeks"
    val isSaved: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
