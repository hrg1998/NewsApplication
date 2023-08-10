package com.hrg.myapplication.data.repository.impl

import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.domain.models.NewsModel
import com.hrg.myapplication.domain.models.ResultNet
import com.hrg.myapplication.domain.models.SearchParametersModel
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    suspend fun getNews(searchParametersModel: SearchParametersModel): ResultNet<NewsModel>

    fun getFavoriteNewsLive(): Flow<List<Article>>

    fun saveFavoriteNews(article: Article)

    fun removeFavorite(article: Article)
}