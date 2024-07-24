package com.tawfek.videojetpackcompose.presentation.ui.screen.video

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.util.Log
import android.view.WindowInsetsController
import androidx.activity.SystemBarStyle
import androidx.activity.compose.BackHandler
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.tawfek.videojetpackcompose.domain.video.model.VideoModel
import com.tawfek.videojetpackcompose.presentation.getActivity

@Composable
fun VideoScreen(
    modifier: Modifier = Modifier,
    videoModel: VideoModel,
    player: Player
) {
    val context = LocalContext.current

    val localConfiguration = LocalConfiguration.current

    val lifecycleOwner = LocalLifecycleOwner.current
    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }

    val windowInsetsController = WindowCompat.getInsetsController(context.getActivity()!!.window, context.getActivity()?.window!!.decorView)
    windowInsetsController.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

    var isFullScreen by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(key1 = isFullScreen) {
        when(isFullScreen) {
            true -> {
                windowInsetsController.hide(WindowInsetsCompat.Type.statusBars())
                context.getActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            false -> {
                windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
                context.getActivity()?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    BackHandler(enabled = localConfiguration.orientation == ORIENTATION_LANDSCAPE){
        // If the video is in full screen, and the user clicked on the back button
        // The default behaviour would be to go back to the home screen
        // So, we customize it to change the orientation to portrait instead of going back to home screen.
        isFullScreen = false
    }

    Column(modifier = modifier.fillMaxSize()) {

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                lifecycle = event
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        AndroidView(
            factory = { context ->
                PlayerView(context).also {
                    it.player = player

                    it.setFullscreenButtonClickListener {fullscreen ->
                        isFullScreen = fullscreen
                    }
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }
                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }
                    else -> Unit
                }
            },
            modifier = if (localConfiguration.orientation == ORIENTATION_LANDSCAPE) Modifier.fillMaxSize() else Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}