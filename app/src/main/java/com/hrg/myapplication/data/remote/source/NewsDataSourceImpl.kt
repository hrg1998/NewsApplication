package com.hrg.myapplication.data.remote.source

import com.hrg.myapplication.data.remote.ApiService
import com.hrg.myapplication.data.remote.impl.NewsDataSource
import com.hrg.myapplication.domain.models.NewsModel
import retrofit2.Response
import javax.inject.Inject

class NewsDataSourceImpl @Inject constructor(private val apiService: ApiService) : NewsDataSource {
    override suspend fun getNews(
        query: String,
        page: Int,
        pageSize: Int,
        sortBy: String,
        searchIn: String?,
        from: String?,
        to: String?
    ): Response<NewsModel> =
        apiService.getNews(query, "f3ea0855fe19420f826a61faa4e82596", page, pageSize, sortBy, searchIn, from, to)
}