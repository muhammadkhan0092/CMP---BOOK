package com.plcoding.bookpedia.book.presentation.book_list

import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.core.presentation.UiText

data class BookListState(
    val searchQuery: String="Kotlin",
    val searchResults : List<Book> = books,
    val favoriteBooks : List<Book> = emptyList(),
    val isLoading : Boolean = false,
    val selectedTabIndex : Int = 0,
    val errorMessage : UiText? = null
)
private val books = listOf(
    Book(
        id = "BOOK I",
        title = "Compose multi-p",
        imageUrl = "",
        authors = listOf("Muhammad khan"),
        description = "This is book i",
        languages =listOf("En","Ur"),
        firstPublishYear ="2001",
        averageRating = 4.9,
        ratingCount = 15,
        numPages = 15,
        numEditions = 1
    )
)