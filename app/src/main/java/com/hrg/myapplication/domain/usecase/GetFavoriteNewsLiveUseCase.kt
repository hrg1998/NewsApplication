package com.hrg.myapplication.domain.usecase

import com.hrg.myapplication.data.repository.impl.NewsRepository
import javax.inject.Inject

class GetFavoriteNewsLiveUseCase @Inject constructor(private val newsRepository: NewsRepository) {
    fun getFavoriteNewsLive() = newsRepository.getFavoriteNewsLive()
}