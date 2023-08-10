package com.hrg.myapplication.data.remote.impl

import com.hrg.myapplication.domain.models.NewsModel
import com.hrg.myapplication.utils.*
import retrofit2.Response
import retrofit2.http.GET

interface NewsDataSource {
    suspend fun getNews(
        query: String,
        page: Int,
        pageSize: Int,
        sortBy: String,
        searchIn: String?,
        from: String?,
        to: String?,
    ): Response<NewsModel>
}