package com.hrg.myapplication.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hrg.myapplication.data.local.dao.NewsDao
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.utils.typeConverter.DBTypeConverter

@Database(
    entities = [Article::class],
    version = NewsDB.versionDatabase,
    exportSchema = false
)
@TypeConverters(DBTypeConverter::class)
abstract class NewsDB : RoomDatabase() {
    abstract fun NewsDao(): NewsDao

    companion object {
        const val databaseName = "NewsDB"
        const val versionDatabase = 1
    }
}