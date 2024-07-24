package com.tawfek.videojetpackcompose.data.video.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface VideoDao {
    @Insert
    suspend fun upsertVideos(videos: List<VideoEntity>)

    @Query("SELECT * FROM videos order by id asc")
    fun getVideos(): PagingSource<Int, VideoEntity>

    @Query("SELECT COUNT(*) FROM videos")
    suspend fun getVideosCount(): Int

    @Query("DELETE FROM videos")
    suspend fun clearVideos()
}