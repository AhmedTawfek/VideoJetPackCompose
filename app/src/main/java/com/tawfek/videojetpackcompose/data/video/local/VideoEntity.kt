package com.tawfek.videojetpackcompose.data.video.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videos")
data class VideoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val comments: Int,
    val downloads: Int,
    val duration: Int,
    val views: Int,
    val likes: Int,
    val tags: String,
    val type: String,
    val userName : String,
    val userImageURL: String,
    val userId: Int,
    val height: Int,
    val size: Int,
    val thumbnail: String,
    val url: String,
    val width: Int
)