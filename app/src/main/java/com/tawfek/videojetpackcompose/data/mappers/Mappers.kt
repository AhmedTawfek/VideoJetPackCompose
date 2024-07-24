package com.tawfek.videojetpackcompose.data.mappers

import com.tawfek.videojetpackcompose.data.video.local.VideoEntity
import com.tawfek.videojetpackcompose.data.video.remote.VideoPostDto
import com.tawfek.videojetpackcompose.domain.use_cases.ErrorType
import com.tawfek.videojetpackcompose.domain.use_cases.FormatDurationUseCase
import com.tawfek.videojetpackcompose.domain.use_cases.FormatViewsCountUseCase
import com.tawfek.videojetpackcompose.domain.use_cases.HandleServerErrorUseCase
import com.tawfek.videojetpackcompose.domain.video.model.VideoModel
import retrofit2.HttpException
import java.io.IOException

fun VideoPostDto.toVideoEntity() = VideoEntity(
        id = 0,
        comments = comments,
        downloads = downloads,
        duration = duration,
        likes = likes,
        views = views,
        tags = tags,
        type = type,
        userName = userName,
        userImageURL = userImageURL,
        userId = userId,
        height = videoDetails.videoDetails.height,
        size = videoDetails.videoDetails.size,
        thumbnail = videoDetails.videoDetails.thumbnail,
        url = videoDetails.videoDetails.url,
        width = videoDetails.videoDetails.width
    )

fun VideoEntity.toVideoModel() = VideoModel(
        id = id,
        comments = comments,
        downloads = downloads,
        duration = duration.toFormatDuration(),
        likes = likes,
        views = views.toViewsCount(),
        tags = tags.split(","),
        type = type,
        userName = userName,
        userImageURL = userImageURL,
        userId = userId,
        thumbnailUrl = thumbnail,
        videoUrl = url
)

fun Int.toViewsCount() = FormatViewsCountUseCase(this)
fun Int.toFormatDuration() = FormatDurationUseCase(this)

fun Exception.getErrorType() : ErrorType {
        return if (this is IOException){
                ErrorType.NetworkNotAvailable
        }else if (this is HttpException){
                HandleServerErrorUseCase().getErrorType(this.code())
        }else{
                ErrorType.UnknownError
        }
}