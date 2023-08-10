package com.hrg.myapplication.domain.usecase

import com.hrg.myapplication.data.repository.impl.NewsRepository
import com.hrg.myapplication.domain.models.SearchParametersModel
import javax.inject.Inject

class GetRemoteNews @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend fun getNews(searchParametersModel: SearchParametersModel) =
        newsRepository.getNews(searchParametersModel)
}