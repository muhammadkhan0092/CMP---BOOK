package com.plcoding.bookpedia.book.data.repository

import androidx.sqlite.SQLiteException
import com.plcoding.bookpedia.book.data.database.FavBookDao
import com.plcoding.bookpedia.book.data.mappers.toBook
import com.plcoding.bookpedia.book.data.mappers.toBookEntity
import com.plcoding.bookpedia.book.domain.RemoteBookDataSource
import com.plcoding.bookpedia.book.domain.Book
import com.plcoding.bookpedia.book.domain.BookRepository
import com.plcoding.bookpedia.core.domain.DataError
import com.plcoding.bookpedia.core.domain.Result
import com.plcoding.bookpedia.core.domain.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultBookRepository(
    private val remoteBookDataSource: RemoteBookDataSource,
    private val favBookDao : FavBookDao
) : BookRepository{
    override suspend fun searchBooks(query : String) : Result<List<Book>, DataError.Remote>{
        return remoteBookDataSource.searchBooks(query).map {dto->
            dto.results.map {
                it.toBook()
            }
        }
    }

    override suspend fun getBookDescription(bookId: String): Result<String?, DataError> {
        val localResult = favBookDao.getFavBook(bookId)
        return if(localResult!=null){
            Result.Success(localResult.description)
        }
        else{
            return remoteBookDataSource.getBookDetails(bookId).map {bookWorkDto->
                bookWorkDto.description
            }
        }
    }

    override fun getFavBooks(): Flow<List<Book>> {
        return favBookDao.getFavBooks().map {bookEntities->
            bookEntities.map { it.toBook() }
        }
    }

    override fun isBookFav(id: String): Flow<Boolean> {
        return favBookDao.getFavBooks().map {entities->
            entities.any { it.id==id }
        }
    }

    override suspend fun markAsFav(book: Book): Result<Unit, DataError.Local> {
        return try {
            favBookDao.upsert(book.toBookEntity())
            Result.Success(Unit)
        }
        catch (e: SQLiteException){
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteFromFav(bookId: String) {
        favBookDao.deleteFavBook(bookId)
    }
}