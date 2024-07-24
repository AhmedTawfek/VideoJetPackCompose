package com.tawfek.videojetpackcompose.data.video.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.tawfek.videojetpackcompose.data.video.local.LocalDatabase
import com.tawfek.videojetpackcompose.data.video.local.VideoEntity
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalPagingApi::class)
@Singleton
class PagerFactory @Inject constructor(
    private val localDatabase: LocalDatabase,
    private val videoApi: VideoApi
) {
    fun createPager(query: String): Pager<Int, VideoEntity> {
        return Pager(
            config = PagingConfig(pageSize = 10, initialLoadSize = 10),
            remoteMediator = RemoteMediator(
                query = query,
                localDatabase = localDatabase,
                videoApi = videoApi
            ),
            pagingSourceFactory = {
                Log.d("Pagination", "pagingSourceFactory Getting data from local.")
                localDatabase.videoDao().getVideos()
            }
        )
    }
}