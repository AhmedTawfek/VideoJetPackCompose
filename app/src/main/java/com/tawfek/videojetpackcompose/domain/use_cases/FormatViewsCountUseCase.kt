package com.tawfek.videojetpackcompose.domain.use_cases

object FormatViewsCountUseCase{
    operator fun invoke(views : Int) : String{
        return when {
            views >= 1_000_000 -> String.format("%.1fM", views / 1_000_000.0)
            views >= 1_000 -> String.format("%.1fk", views / 1_000.0)
            else -> views.toString()
        }
    }
}