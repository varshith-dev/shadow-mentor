package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "learning_nodes")
data class LearningNode(
    @PrimaryKey val id: String, // e.g. "python_foundations", "data_wrangling", "ml_basics"
    val title: String,
    val status: String, // "completed", "in_progress", "locked"
    val percentage: Int, // e.g. 100 for completed, 60 for in_progress, 0 for locked
    val subtitle: String, // e.g. "Completed Jan 12" or "Module 4"
    val cardText: String? = null, // e.g. "Certificate Earned" or "Project: Data Scraper"
    val isCertificate: Boolean = false,
    val order: Int
)
