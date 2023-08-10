package com.hrg.myapplication.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hrg.myapplication.domain.models.Article
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {
    @Insert
    fun addFavoriteArticle(article: Article)

    @Query("SELECT * FROM article")
    fun getArticlesLive(): Flow<List<Article>>

    @Delete
    fun remove(article: Article)
}