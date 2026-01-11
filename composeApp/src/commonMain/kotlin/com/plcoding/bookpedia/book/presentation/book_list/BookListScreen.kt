package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.bookpedia.book.domain.Book
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.plcoding.bookpedia.book.presentation.book_list.components.BookSearchBar
import com.plcoding.bookpedia.core.presentation.DarkBlue


@Composable
fun BookListScreenRoot(
    viewModel : BookListVm = koinViewModel(),
    onBookClick : (Book)-> Unit
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    BookListScreen(state, { action->
        when(action){
            is BookListAction.OnBookClick->onBookClick(action.book)
            else -> Unit
        }
        viewModel.onAction(action)
    }
    )
}
@Composable
private fun BookListScreen(
    state : BookListState,
    onAction : (BookListAction)-> Unit
){
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBlue)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        BookSearchBar(
            state.searchQuery,
            onSearchQueryChanged = {
                onAction(BookListAction.OnSearchQueryChanged(it))
            },
            onImeiSearch = {
                keyboardController?.hide()
            },
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
                .padding(60.dp)
        )
    }
}