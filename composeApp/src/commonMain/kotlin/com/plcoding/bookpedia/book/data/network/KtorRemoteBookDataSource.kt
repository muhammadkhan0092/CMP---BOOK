package com.plcoding.bookpedia.book.data.network

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import io.ktor.client.HttpClient

class KtorRemoteBookDataSource(
    private val httpClient : HttpClient
) {
    suspend fun searchBooks(query : String,resultLimit : Int? = null) : Result<Book, DataError.Remote>{

    }
}