package com.tawfek.videojetpackcompose.data.video.remote

import com.google.gson.annotations.SerializedName

data class VideoPostResponse(
    val total: Int,
    val totalHits: Int,
    @SerializedName("hits") val videoPosts : List<VideoPostDto>)

data class VideoPostDto(
    val id: Int,
    val comments: Int,
    val downloads: Int,
    val duration: Int,
    val likes: Int,
    val tags: String,
    val type: String,
    @SerializedName("user") val userName : String,
    val userImageURL: String,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("videos") val videoDetails : VideosDto,
    val views: Int
)

data class VideosDto(
    @SerializedName("medium") val videoDetails : VideoDetails
)

data class VideoDetails(
    val height: Int,
    val size: Int,
    val thumbnail: String,
    val url: String,
    val width: Int
)