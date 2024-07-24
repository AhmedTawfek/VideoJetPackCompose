package com.tawfek.videojetpackcompose.presentation.ui.screen.video

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.tawfek.videojetpackcompose.domain.video.model.VideoModel
import com.tawfek.videojetpackcompose.presentation.ui.screen.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoViewModel @Inject constructor(
    val player: Player
) : ViewModel() {
    private val _currentSelectedVideo = MutableStateFlow(VideoModel())
    val currentSelectedVideoModel = _currentSelectedVideo.asStateFlow()

    var mediaItemIsSet = false

    fun setSelectedVideoModel(videoModel: VideoModel) {
        _currentSelectedVideo.value = videoModel
        setMediaItem()
    }

    fun setMediaItem(){
        if (!mediaItemIsSet) {
            player.setMediaItem(MediaItem.fromUri(currentSelectedVideoModel.value.videoUrl))
            player.prepare()
            mediaItemIsSet = true
        }
    }

    fun getSelectedVideoModel(): VideoModel {
        return _currentSelectedVideo.value
    }

    fun playVideo(videoModel: VideoModel) {
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}