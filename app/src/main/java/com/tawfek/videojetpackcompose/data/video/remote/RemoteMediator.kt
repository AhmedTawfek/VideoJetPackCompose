package com.tawfek.videojetpackcompose.data.video.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.tawfek.videojetpackcompose.data.mappers.toVideoEntity
import com.tawfek.videojetpackcompose.data.video.local.LocalDatabase
import com.tawfek.videojetpackcompose.data.video.local.VideoEntity
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagingApi::class)
class RemoteMediator(
    private val query : String,
    private val localDatabase: LocalDatabase,
    private val videoApi : VideoApi
) : RemoteMediator<Int, VideoEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, VideoEntity>
    ): MediatorResult {
        try {
            delay(500) // For better UX experience, for testing purposes only.

            val videosDao = localDatabase.videoDao()

            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d("Pagination", "LoadType is Refresh")
                    1
                }

                LoadType.PREPEND -> {
                    Log.d("Pagination", "LoadType is Prepend")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    Log.d("Pagination", "LoadType is Append")
                    val lastItem = state.lastItemOrNull()

                    if (lastItem == null) {
                        Log.d("Pagination", "No items found. Setting loadKey to 1.")
                        1
                    } else {
                        val totalVideosCountExistsLocally = videosDao.getVideosCount()
                        Log.d("Pagination", "Total videos count locally: $totalVideosCountExistsLocally")
                        val loadKey = (totalVideosCountExistsLocally / state.config.pageSize) + 1
                        Log.d("Pagination", "Computed loadKey: $loadKey")
                        loadKey
                    }
                }
            }

            Log.d("Pagination", "loadKey is: $loadKey")

            val postsResponse = videoApi.searchForVideos(
                apiKey = VideoApi.API_KEY,
                query = query,
                page = loadKey,
                perPage = state.config.pageSize
            )

            Log.d("Pagination", "ResponseResult = $postsResponse")

            localDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    videosDao.clearVideos()
                }

                val videoEntities = postsResponse.videoPosts.map { it.toVideoEntity() }
                localDatabase.videoDao().upsertVideos(videoEntities)
            }

            return MediatorResult.Success(endOfPaginationReached = postsResponse.videoPosts.isEmpty())
        } catch (exception : Exception) {
            Log.d("Pagination", "Exception In ResponseResult =${exception.message} ${exception.localizedMessage}")
            return MediatorResult.Error(exception)
        }
    }
}