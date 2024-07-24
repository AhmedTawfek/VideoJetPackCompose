package com.tawfek.videojetpackcompose.presentation.ui.screen.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.tawfek.videojetpackcompose.data.mappers.toVideoModel
import com.tawfek.videojetpackcompose.data.video.local.VideoEntity
import com.tawfek.videojetpackcompose.data.video.remote.PagerFactory
import com.tawfek.videojetpackcompose.domain.video.model.VideoModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pager: PagerFactory,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _currentSelectedVideo = MutableStateFlow(VideoModel())
    val currentSelectedVideo = _currentSelectedVideo.asStateFlow()

    val searchVideosQuery = savedStateHandle.getStateFlow("query_video", "")

    fun setCurrentSelectedVideo(video: VideoModel) {
        _currentSelectedVideo.value = video
        //savedStateHandle["current_selected_video_model"] = video
    }

    fun setVideoQuery(newQuery: String) {
        Log.d("Pagination", "setVideoQuery is called : $newQuery")
        savedStateHandle["query_video"] = newQuery
    }

    val videosPagerFlow = savedStateHandle.getStateFlow("query_video", "")
        .debounce(500)
        .flatMapLatest { query ->
        pager.createPager(query).flow.map { pagingData ->
            pagingData.map { it.toVideoModel() }
        }
    }.cachedIn(viewModelScope)
}