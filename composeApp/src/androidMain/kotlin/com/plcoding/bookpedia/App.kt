package com.plcoding.bookpedia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListVm

@Composable
fun App(){
    BookListScreenRoot(
        viewModel = remember { BookListVm() },
        onBookClick = {}
    )
}