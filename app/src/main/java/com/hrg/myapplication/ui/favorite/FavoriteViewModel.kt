package com.hrg.myapplication.ui.favorite

import com.hrg.myapplication.domain.usecase.GetFavoriteNewsLiveUseCase
import com.hrg.myapplication.utils.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteNewsLiveUseCase: GetFavoriteNewsLiveUseCase
) : BaseViewModel() {

    fun getFavNews() = getFavoriteNewsLiveUseCase.getFavoriteNewsLive()
}