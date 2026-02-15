package com.plcoding.bookpedia.book.data.mappers

import com.plcoding.bookpedia.book.data.database.BookEntity
import com.plcoding.bookpedia.book.data.dto.SearchedBookDto
import com.plcoding.bookpedia.book.domain.Book

fun SearchedBookDto.toBook() : Book{
    return Book(
        id = id.substringAfterLast("/"),
        title = title,
        imageUrl = if (coverKey!=null) {
            "https://covers.openlibrary.org/b/olid${coverKey}-L.jpg"
        }
        else{
            "https://covers.openlibrary.org/b/olid${coverKey}-L.jpg"
        },
        authors = authorNames?:emptyList(),
        description = null,
        languages = languages?:emptyList(),
        firstPublishYear = firstPublishedYear.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions?:0
    )
}


fun Book.toBookEntity() : BookEntity {
    return BookEntity(
        id = id,
        title = title,
        imageUrl = imageUrl,
        authors = authors,
        description = description,
        languages = languages,
        firstPublishYear = firstPublishYear,
        ratingsAverage = averageRating,
        ratingsCount = ratingCount,
        numPagesMedian = numPages,
        numEditions = numEditions
    )
}

fun BookEntity.toBook() : Book{
    return Book(
        id = id.substringAfterLast("/"),
        title = title,
        imageUrl = if (imageUrl!=null) {
            "https://covers.openlibrary.org/b/olid${imageUrl}-L.jpg"
        }
        else{
            "https://covers.openlibrary.org/b/olid${imageUrl}-L.jpg"
        },
        authors = authors,
        description = null,
        languages = languages,
        firstPublishYear = firstPublishYear.toString(),
        averageRating = ratingsAverage,
        ratingCount = ratingsCount,
        numPages = numPagesMedian,
        numEditions = numEditions
    )
}