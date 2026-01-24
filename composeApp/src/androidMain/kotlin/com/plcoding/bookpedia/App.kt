package com.plcoding.bookpedia

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.plcoding.bookpedia.book.data.network.KtorRemoteBookDataSource
import com.plcoding.bookpedia.book.data.repository.DefaultBookRepository
import com.plcoding.bookpedia.book.domain.RemoteBookDataSource
import com.plcoding.bookpedia.book.presentation.book_list.BookListScreenRoot
import com.plcoding.bookpedia.book.presentation.book_list.BookListVm
import com.plcoding.bookpedia.core.data.HttpClientFactory
import io.ktor.client.engine.HttpClientEngine

@Composable
fun App(engine : HttpClientEngine){
    BookListScreenRoot(
        viewModel = remember { BookListVm(
            DefaultBookRepository(
                KtorRemoteBookDataSource(
                    httpClient = HttpClientFactory.create(engine)
                )
            )
        ) },
        onBookClick = {}
    )
}