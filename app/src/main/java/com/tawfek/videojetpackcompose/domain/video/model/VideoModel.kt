package com.tawfek.videojetpackcompose.domain.video.model

import android.os.Parcelable
import kotlinx.serialization.Serializable

@Serializable
data class VideoModel(
    val id: Int = 0,
    val comments: Int = 0,
    val downloads: Int = 0,
    val duration: String = "0",
    val views: String = "0",
    val likes: Int = 0,
    val tags: List<String> = emptyList(),
    val type: String = "",
    val userName : String = "",
    val userImageURL: String = "",
    val userId: Int = 0,
    val thumbnailUrl : String = "",
    val videoUrl: String = ""
)