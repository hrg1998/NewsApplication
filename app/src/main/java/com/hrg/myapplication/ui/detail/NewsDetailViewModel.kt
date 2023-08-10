package com.hrg.myapplication.ui.detail

import androidx.lifecycle.viewModelScope
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.domain.usecase.GetFavoriteNewsLiveUseCase
import com.hrg.myapplication.domain.usecase.RemoveFavoriteNewsUseCase
import com.hrg.myapplication.domain.usecase.SaveFavoriteNewsUseCase
import com.hrg.myapplication.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsDetailViewModel @Inject constructor(
    private val saveFavoriteNewsUseCase: SaveFavoriteNewsUseCase,
    private val removeFavoriteNewsUseCase: RemoveFavoriteNewsUseCase,
    private val getFavoriteNewsLiveUseCase: GetFavoriteNewsLiveUseCase
) : BaseViewModel() {

    private val favList: ArrayList<Article> = ArrayList()

    fun getAllFavNewsLive() = getFavoriteNewsLiveUseCase.getFavoriteNewsLive()

    fun saveFav(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            saveFavoriteNewsUseCase.saveFavoriteNews(article)
        }
    }

    fun removeFav(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            removeFavoriteNewsUseCase.removeFavoriteNews(article)
        }
    }

    fun updateFavList(list: List<Article>) {
        favList.clear()
        favList.addAll(list)
    }

    fun checkExistArticleInFavList(article: Article) = favList.contains(article)
}