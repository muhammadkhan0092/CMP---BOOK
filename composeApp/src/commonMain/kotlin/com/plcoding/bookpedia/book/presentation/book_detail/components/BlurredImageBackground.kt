package com.plcoding.bookpedia.book.presentation.book_detail.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.book_cover
import cmp_bookpedia.composeapp.generated.resources.book_error_2
import cmp_bookpedia.composeapp.generated.resources.go_back
import cmp_bookpedia.composeapp.generated.resources.mark_as_favorite
import cmp_bookpedia.composeapp.generated.resources.remove_from_favorites
import coil3.compose.rememberAsyncImagePainter
import com.plcoding.bookpedia.core.presentation.DarkBlue
import com.plcoding.bookpedia.core.presentation.DesertWhite
import com.plcoding.bookpedia.core.presentation.PulseAnimation
import com.plcoding.bookpedia.core.presentation.SandYellow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun BlurredImageBackground(
    imageUrl: String?,
    isFav: Boolean,
    onFavClicked: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    println("IMAGE URL IS $imageUrl")
    var imageLoadResult by remember {
        mutableStateOf<Result<Painter>?>(null)
    }
    val painter = rememberAsyncImagePainter(
        model = imageUrl,
        onSuccess = {
            val size = it.painter.intrinsicSize
            imageLoadResult = if (size.width > 1 && size.height > 1) {
                Result.success(it.painter)
            } else Result.failure(Exception("Invalid image dimensions"))
        },
        onError = {
            imageLoadResult = Result.failure(Exception("Other Error"))
        }
    )
    Box() {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BlurredBox(
                Modifier
                    .weight(0.3f),
                imageLoadResult = imageLoadResult
            )
            Box(modifier = Modifier.weight(0.7f).fillMaxWidth().background(DesertWhite)) {
            }
        }
        BackButtonComposable(
            modifier = Modifier.align(Alignment.TopStart),
            onBackClicked = onBackClicked
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))
            ElevatedCard(
                modifier = Modifier
                    .width(200.dp)
                    .aspectRatio(2 / 3f),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = Color.Transparent
                ),
                elevation = CardDefaults.elevatedCardElevation(
                    defaultElevation = 15.dp
                )
            ) {
                AnimatedContent(
                    targetState = imageLoadResult
                ) { result ->
                    when {
                        result == null -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                PulseAnimation()
                            }
                        }
                        else -> {
                            Box() {
                                Image(
                                    painter = if (result.isSuccess) painter else painterResource(Res.drawable.book_error_2),
                                    contentDescription = stringResource(Res.string.book_cover),
                                    modifier = Modifier.fillMaxSize()
                                        .background(Color.Transparent),
                                    contentScale = if (result.isSuccess) ContentScale.Crop else ContentScale.Fit
                                )
                                FavButtonComposable(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd),
                                    onFavClicked = onFavClicked,
                                    isFav = isFav
                                )
                            }
                        }
                    }
                }
            }
            content()
        }
    }
}

@Composable
fun BlurredBox(modifier: Modifier, imageLoadResult: Result<Painter>?) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkBlue)
    ) {
        imageLoadResult?.getOrNull()?.let { painter ->
            Image(
                painter = painter,
                contentDescription = stringResource(Res.string.book_cover),
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().blur(20.dp)
            )
        }
    }
}

@Composable()
fun FavButtonComposable(modifier: Modifier, onFavClicked: () -> Unit, isFav: Boolean) {
    IconButton(
        onClick = onFavClicked,
        modifier = modifier
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(SandYellow, Color.Transparent),
                    radius = 70f
                )
            )
    ) {
        Icon(
            imageVector = if (isFav) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
            contentDescription = if (isFav) stringResource(Res.string.remove_from_favorites) else stringResource(
                Res.string.mark_as_favorite
            ),
            tint = Color.Red,
        )
    }
}

@Composable
fun BackButtonComposable(modifier: Modifier, onBackClicked: () -> Unit) {
    IconButton(
        onClick = onBackClicked,
        modifier = modifier
            .padding(top = 16.dp, start = 16.dp)
            .statusBarsPadding(),
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(Res.string.go_back),
            tint = Color.White
        )
    }
}