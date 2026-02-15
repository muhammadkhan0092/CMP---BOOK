package com.plcoding.bookpedia.book.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [BookEntity::class],
    version = 1
)
@TypeConverters(
    StringListTypeConverter::class
)
abstract class FavBookDatabase : RoomDatabase() {
    abstract val favBookDao : FavBookDao

    companion object {
        const val DB_NAME = "bookpedia"
    }
}