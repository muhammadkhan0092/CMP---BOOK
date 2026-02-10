package com.plcoding.bookpedia.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailScreenRoot
import com.plcoding.bookpedia.book.presentation.book_detail.BookDetailViewModel
import com.plcoding.bookpedia.book.presentation.book_detail.models.BookDetailAction
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListVm
import com.plcoding.bookpedia.book.presentation.book_list.SelectedBookViewModel
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun App() {
    MaterialTheme {
        val viewModel = koinViewModel<BookListVm>()
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = Route.BookGraph
        ){
            navigation<Route.BookGraph>(startDestination = Route.BookList){
                composable<Route.BookList>{
                    val selectedVm = it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    LaunchedEffect(true) {
                        selectedVm.onSelectBook(null)
                    }
                    BookListScreenRoot(
                        viewModel = remember { viewModel },
                        onBookClick = {book->
                            selectedVm.onSelectBook(book)
                            navController.navigate(Route.BookDetail(book.id))
                        }
                    )
                }
                composable<Route.BookDetail>{
                    val selectedVm = it.sharedKoinViewModel<SelectedBookViewModel>(navController)
                    val bookDetailVm = koinViewModel<BookDetailViewModel>()
                    val selectedBook by selectedVm.selectedBook.collectAsStateWithLifecycle()
                    LaunchedEffect(selectedBook){
                        selectedBook?.let {
                            bookDetailVm.onAction(BookDetailAction.OnSelectedBookChange(it))
                        }
                    }
                    BookDetailScreenRoot(
                        viewModel = bookDetailVm,
                        onBackClick = {
                            navController.navigateUp()
                        }
                    )
                }
            }
        }
    }
}
@Composable
private inline fun <reified  T: ViewModel> NavBackStackEntry.sharedKoinViewModel(
    navController: NavController
):T{
    val navGraphRoute = destination.parent?.route?:return koinViewModel<T>()
    val parentEntry = remember(this){
        navController.getBackStackEntry(navGraphRoute)
    }
    return koinViewModel(viewModelStoreOwner = parentEntry)
}