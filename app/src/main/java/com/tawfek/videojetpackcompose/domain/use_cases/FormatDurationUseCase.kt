package com.tawfek.videojetpackcompose.domain.use_cases

object FormatDurationUseCase {
    operator fun invoke(duration : Int): String {
        return when {
            duration < 60 -> {
                String.format("0:%02d", duration)
            }
            duration < 3600 -> {
                val minutes = duration / 60
                val seconds = duration % 60
                String.format("%d:%02d", minutes, seconds)
            }
            else -> {
                val hours = duration / 3600
                val minutes = (duration % 3600) / 60
                val seconds = duration % 60
                String.format("%d:%02d:%02d", hours, minutes, seconds)
            }
        }
    }
}