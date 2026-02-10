package com.plcoding.bookpedia.book.presentation.book_detail.models

import com.plcoding.bookpedia.book.domain.Book

sealed interface BookDetailAction {
    data object OnBackClick: BookDetailAction
    data object OnFavClick: BookDetailAction
    data class OnSelectedBookChange(val book : Book): BookDetailAction
}