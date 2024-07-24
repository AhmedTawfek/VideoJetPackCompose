package com.tawfek.videojetpackcompose.data.video.remote

import com.tawfek.videojetpackcompose.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface VideoApi {
    @GET("api/videos/")
    suspend fun searchForVideos(
        @Query("key") apiKey : String,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ) : VideoPostResponse

    companion object{
        const val BASE_URL = "https://pixabay.com/"
        const val API_KEY = BuildConfig.API_KEY
    }
}