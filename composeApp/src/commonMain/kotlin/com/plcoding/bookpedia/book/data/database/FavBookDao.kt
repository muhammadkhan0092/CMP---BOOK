package com.plcoding.bookpedia.book.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface FavBookDao {

    @Upsert
    suspend fun upsert(book : BookEntity)

    @Query("SELECT * FROM book_entity")
    fun getFavBooks() : Flow<List<BookEntity>>

    @Query("SELECT * FROM book_entity WHERE id= :id")
    suspend fun getFavBook(id : String) : BookEntity?

    @Query("DELETE FROM book_entity WHERE id= :id")
    suspend fun deleteFavBook(id : String)
}