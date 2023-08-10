package com.hrg.myapplication.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.domain.models.NewsModel
import com.hrg.myapplication.domain.models.ResultNet
import com.hrg.myapplication.domain.models.SearchParametersModel
import com.hrg.myapplication.domain.usecase.GetFavoriteNewsLiveUseCase
import com.hrg.myapplication.domain.usecase.GetRemoteNews
import com.hrg.myapplication.domain.usecase.SaveFavoriteNewsUseCase
import com.hrg.myapplication.utils.PAGINATION_PAGE_SIZE
import com.hrg.myapplication.utils.SearchIn
import com.hrg.myapplication.utils.SortBy
import com.hrg.myapplication.utils.base.BaseViewModel
import com.hrg.myapplication.utils.pagingSource.GenericPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getRemoteNews: GetRemoteNews,
) : BaseViewModel() {
    private val _searchParametersModel = SearchParametersModel(q = "")
    val searchParametersModel: SearchParametersModel
        get() = _searchParametersModel

    val newsPaging = Pager(PagingConfig(pageSize = PAGINATION_PAGE_SIZE)) {
        GenericPagingSource(mConnectionNet, mPageSize = PAGINATION_PAGE_SIZE) { page, pagination ->
            if (_searchParametersModel.q.isEmpty()) {
                return@GenericPagingSource ResultNet.Success<NewsModel>(NewsModel(null, null, null))
            } else {
                return@GenericPagingSource getRemoteNews.getNews(_searchParametersModel.apply {
                    this.page = page
                    this.pageSize = pagination
                })
            }
        }
    }.flow.cachedIn(viewModelScope)

    fun updateSearchParameter(
        query: String = _searchParametersModel.q,
        sortBy: SortBy = _searchParametersModel.sortBy,
        searchIn: ArrayList<SearchIn> = _searchParametersModel.searchIn,
        from: String? = _searchParametersModel.from,
        to: String? = _searchParametersModel.to
    ) {
        _searchParametersModel.apply {
            this.q = query
            this.sortBy = sortBy
            this.searchIn = searchIn
            this.from = from
            this.to = to
        }
    }
}