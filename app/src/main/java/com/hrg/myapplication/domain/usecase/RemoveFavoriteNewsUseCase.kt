package com.hrg.myapplication.domain.usecase

import com.hrg.myapplication.data.repository.impl.NewsRepository
import com.hrg.myapplication.domain.models.Article
import javax.inject.Inject

class RemoveFavoriteNewsUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    fun removeFavoriteNews(article: Article) = newsRepository.removeFavorite(article)
}