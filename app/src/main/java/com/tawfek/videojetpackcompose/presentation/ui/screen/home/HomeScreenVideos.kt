package com.tawfek.videojetpackcompose.presentation.ui.screen.home

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.tawfek.videojetpackcompose.R
import com.tawfek.videojetpackcompose.data.mappers.getErrorType
import com.tawfek.videojetpackcompose.domain.video.model.VideoModel
import com.tawfek.videojetpackcompose.presentation.ui.theme.VideoJetPackComposeTheme
import androidx.paging.compose.items
import com.tawfek.videojetpackcompose.presentation.ui.utils.convertErrorTypeToMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenVideos(
    modifier: Modifier = Modifier,
    videos : LazyPagingItems<VideoModel>,
    searchTextQuery : String = "",
    onQueryChange : (String) -> Unit,
    onItemClick : (VideoModel) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(key1 = videos.loadState) {
        if (videos.loadState.refresh is LoadState.Error) {
            val errorType = ((videos.loadState.refresh as LoadState.Error).error as Exception).getErrorType()

            Toast.makeText(
                context,
                errorType.convertErrorTypeToMessage(context),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (videos.loadState.refresh is LoadState.Loading && videos.itemCount == 0) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else if (videos.loadState.refresh is LoadState.Error && videos.itemCount == 0){
            Column(modifier = Modifier.fillMaxSize(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                Image(painter = painterResource(id = R.drawable.no_internet_icon), contentDescription = "No Internet")

                Button(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .padding(top = 10.dp),
                    onClick = { videos.retry() }) {
                    Text(text = "Try Again.")
                }
            }
        } else {

            if (videos.loadState.refresh is LoadState.Loading) {
                Dialog(onDismissRequest = {  }) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center)
                        .zIndex(1f))
                }
            }

            Column(modifier = modifier.fillMaxSize()) {

                val interactionSource = remember { MutableInteractionSource() }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    onClick = { },
                    shape = RoundedCornerShape(25.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {

                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.Transparent),
                        value = searchTextQuery,
                        onValueChange = { query ->
                            onQueryChange(query)
                        },
                        interactionSource = interactionSource
                    ) {

                        TextFieldDefaults.DecorationBox(
                            value = searchTextQuery,
                            innerTextField = it,
                            enabled = true,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = interactionSource,
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.search_icon),
                                    contentDescription = "Search"
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    onQueryChange("")
                                }) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.clear_icon),
                                        contentDescription = "Clear"
                                    )
                                }
                            },
                            placeholder = {
                                Text(text = "Search For Videos")
                            },
                            container = {
                                // Customizing the container to remove the bottom line ( Divider )
                            }
                        )
                    }
                }

                val listState = rememberLazyListState()

                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    items(videos){videoItem ->
                        if (videoItem!=null){
                            VideoPost(videoModel = videoItem){
                                onItemClick(it)
                            }
                        }
                    }
                    
                    item {
                        if (videos.loadState.append is LoadState.Loading) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VideoPost(
    modifier: Modifier = Modifier,
    videoModel: VideoModel,
    onItemClick: (VideoModel) -> Unit) {

    Column(modifier = modifier.padding(horizontal = 15.dp, vertical = 5.dp).clickable {
        onItemClick(videoModel)
    }) {

        Box(modifier = Modifier.height(160.dp)) {

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(videoModel.thumbnailUrl)
                    .crossfade(false)
                    .placeholderMemoryCacheKey(videoModel.thumbnailUrl) //  same key as shared element key
                    .memoryCacheKey(videoModel.thumbnailUrl) // same key as shared element key
                    .build(), contentDescription = null, modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(ShapeDefaults.Small)
                , contentScale = ContentScale.FillBounds, placeholder = painterResource(id = R.drawable.pixeled_background), error = painterResource(
                    id = R.drawable.pixeled_background
                )
            )

            ViewsBox(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                text = "${videoModel.views} views"
            )
            ViewsBox(
                modifier = Modifier
                    .padding(10.dp)
                    .align(Alignment.BottomEnd),
                text = "${videoModel.duration}"
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = videoModel.userImageURL,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.profile_icon),
                error = painterResource(id = R.drawable.profile_icon)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(text = videoModel.userName, modifier = Modifier.align(Alignment.CenterVertically))
        }

        FlowRow(modifier = Modifier.absolutePadding(left = 40.dp)) {
            videoModel.tags.forEach {
                ChipItem(tagText = "#$it")
            }
        }
        
        Spacer(modifier = Modifier.height(15.dp))
        HorizontalDivider()
    }
}

@Composable
fun ViewsBox(modifier: Modifier = Modifier, text: String) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.6f)),
        shape = ShapeDefaults.Small
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
            text = text, color = Color.White, fontSize = 11.sp
        )
    }
}

@Composable
fun ChipItem(modifier: Modifier = Modifier, tagText: String) {
    Card(
        modifier = modifier.padding(horizontal = 5.dp, vertical = 5.dp),
        shape = ShapeDefaults.Medium,
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE5E8ED))
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
            text = tagText, fontSize = 13.sp, color = Color(0xFF878C9F)
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {

    VideoJetPackComposeTheme {
        Surface(color = Color.White) {
            //HomeScreenVideos()
        }
    }

}