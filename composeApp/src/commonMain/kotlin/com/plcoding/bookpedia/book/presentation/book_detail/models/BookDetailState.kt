package com.plcoding.bookpedia.book.presentation.book_detail.models

import com.plcoding.bookpedia.book.domain.Book

data class BookDetailState(
    val isLoading : Boolean = true,
    val isFav : Boolean = false,
    val book : Book? = null
)