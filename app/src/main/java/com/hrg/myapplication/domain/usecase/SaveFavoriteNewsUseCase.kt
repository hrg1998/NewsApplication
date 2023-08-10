package com.hrg.myapplication.domain.usecase

import com.hrg.myapplication.data.repository.impl.NewsRepository
import com.hrg.myapplication.domain.models.Article
import javax.inject.Inject

class SaveFavoriteNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    fun saveFavoriteNews(article: Article) {
        newsRepository.saveFavoriteNews(article)
    }
}