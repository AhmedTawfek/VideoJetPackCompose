package com.tawfek.videojetpackcompose.presentation.ui.screen.navigation

import kotlinx.serialization.Serializable

interface Screen{
    val route : String
    val title : String
}

@Serializable
object Home : Screen {
    override val route = "home"
    override val title = "Home"
}

@Serializable
object Video : Screen {
    override val route = "video"
    override val title = "Video"
}