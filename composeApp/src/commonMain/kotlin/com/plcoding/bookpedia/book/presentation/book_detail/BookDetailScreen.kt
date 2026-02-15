package com.plcoding.bookpedia.book.presentation.book_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmp_bookpedia.composeapp.generated.resources.Res
import cmp_bookpedia.composeapp.generated.resources.description_unavailable
import cmp_bookpedia.composeapp.generated.resources.languages
import cmp_bookpedia.composeapp.generated.resources.pages
import cmp_bookpedia.composeapp.generated.resources.rating
import cmp_bookpedia.composeapp.generated.resources.synopsis
import com.plcoding.bookpedia.book.presentation.book_detail.components.BlurredImageBackground
import com.plcoding.bookpedia.book.presentation.book_detail.components.BookChip
import com.plcoding.bookpedia.book.presentation.book_detail.components.ChipSize
import com.plcoding.bookpedia.book.presentation.book_detail.components.TitledContent
import com.plcoding.bookpedia.book.presentation.book_detail.models.BookDetailAction
import com.plcoding.bookpedia.book.presentation.book_detail.models.BookDetailState
import org.jetbrains.compose.resources.stringResource

@Composable
fun BookDetailScreenRoot(
    viewModel: BookDetailViewModel,
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookDetailScreen(
        state,
        onAction = { action ->
            when (action) {
                BookDetailAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun BookDetailScreen(
    state: BookDetailState,
    onAction: (BookDetailAction) -> Unit
) {
    BlurredImageBackground(
        imageUrl = state.book?.imageUrl,
        isFav = state.isFav,
        onFavClicked = { onAction(BookDetailAction.OnFavClick) },
        onBackClicked = {
            onAction(BookDetailAction.OnBackClick)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        if (state.book != null) {
            Column(
                modifier = Modifier
                    .widthIn(max = 700.dp)
                    .fillMaxSize()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        state.book.title,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        state.book.authors.joinToString(", "),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        state.book.averageRating?.let {
                            RatingAndPages(
                                stringResource(Res.string.rating),
                                state.book.averageRating.toString(),
                                true
                            )
                        }
                        state.book.numPages?.let {
                            RatingAndPages(
                                stringResource(Res.string.pages),
                                state.book.numPages.toString()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    if (state.book.languages.isNotEmpty()) {
                        Text(stringResource(Res.string.languages))
                        state.book
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.wrapContentSize(Alignment.Center)
                        ) {
                            state.book.languages.forEach { language ->
                                BookChip(modifier = Modifier.padding(2.dp), size = ChipSize.SMALL) {
                                    Text(
                                        language,
                                        color = Color.Black,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                Text(
                    stringResource(Res.string.synopsis),
                    style = MaterialTheme.typography.titleLarge
                )
                val description = when (state.book.description) {
                    null -> stringResource(Res.string.description_unavailable)
                    else -> state.book.description
                }
                if(state.isLoading){
                    Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center){
                        CircularProgressIndicator()
                    }
                }
                else{
                    Text(
                        text = description,
                        textAlign = TextAlign.Justify,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if(state.book.description.isNullOrBlank()) Color.Black.copy(alpha = 0.4f) else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingAndPages(
    heading: String,
    contentText: String,
    isIcon: Boolean = false
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TitledContent(
            title = heading
        ) {
            Row {
                BookChip(modifier = Modifier, size = ChipSize.REGULAR) {
                    Text(contentText, color = Color.Black)
                    if (isIcon) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = stringResource(Res.string.rating),
                            tint = Color.Yellow
                        )
                    }
                }
            }
        }
    }
}