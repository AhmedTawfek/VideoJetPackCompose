@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.tawfek.videojetpackcompose.presentation.ui.screen.navigation

import android.util.Log
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.tawfek.videojetpackcompose.domain.video.model.VideoModel
import com.tawfek.videojetpackcompose.presentation.ui.screen.home.HomeScreenVideos
import com.tawfek.videojetpackcompose.presentation.ui.screen.home.HomeViewModel
import com.tawfek.videojetpackcompose.presentation.ui.screen.video.VideoScreen
import com.tawfek.videojetpackcompose.presentation.ui.screen.video.VideoViewModel

@Composable
fun Navigation(navHostController: NavHostController,modifier: Modifier = Modifier) {

    SharedTransitionLayout {
        NavHost(navController = navHostController, startDestination = Home) {

            composable<Home> {
                val viewModel = hiltViewModel<HomeViewModel>()
                val videos = viewModel.videosPagerFlow.collectAsLazyPagingItems()
                val searchQuery = viewModel.searchVideosQuery.collectAsStateWithLifecycle()

                HomeScreenVideos(
                    modifier = modifier,
                    videos = videos,
                    searchTextQuery = searchQuery.value,
                    onQueryChange = {
                        viewModel.setVideoQuery(it)
                    }
                ) { videoModel ->
                    viewModel.setCurrentSelectedVideo(videoModel)
                    navHostController.navigate(videoModel)
                }
            }

            composable<VideoModel> { backStackEntry ->
                val videoModel = backStackEntry.toRoute<VideoModel>()
                val viewModel = hiltViewModel<VideoViewModel>()

                viewModel.setSelectedVideoModel(videoModel)

                VideoScreen(videoModel = videoModel,player = viewModel.player, modifier = modifier)
            }
        }
    }
}