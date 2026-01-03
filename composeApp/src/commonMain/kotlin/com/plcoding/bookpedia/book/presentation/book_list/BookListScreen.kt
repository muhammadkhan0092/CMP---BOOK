package com.plcoding.bookpedia.book.presentation.book_list

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.plcoding.bookpedia.book.domain.Book
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.getValue


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

}