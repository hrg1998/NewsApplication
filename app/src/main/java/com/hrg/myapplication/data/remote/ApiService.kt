package com.hrg.myapplication.data.remote

import com.hrg.myapplication.domain.models.NewsModel
import com.hrg.myapplication.utils.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query(QUERY_PARAMETER_QUERY) query: String,
        @Query(QUERY_PARAMETER_API_KEY) apiKey: String,
        @Query(QUERY_PARAMETER_PAGE) page: Int,
        @Query(QUERY_PARAMETER_PAGE_SIZE) pageSize: Int,
        @Query(QUERY_PARAMETER_SORT_BY) sortBy: String,
        @Query(QUERY_PARAMETER_SEARCH_IN) searchIn: String?,
        @Query(QUERY_PARAMETER_FROM) from: String?,
        @Query(QUERY_PARAMETER_TO) to: String?,
    ): Response<NewsModel>
}