package com.hrg.myapplication.utils.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.hrg.myapplication.domain.models.CheckErrorApiClass
import com.hrg.myapplication.domain.models.NewsModel
import com.hrg.myapplication.domain.models.ResultNet
import com.hrg.myapplication.utils.ERROR_CHECK_NETWORK
import com.hrg.myapplication.utils.PAGINATION_PAGE_SIZE
import com.hrg.myapplication.utils.networkHelper.ConnectionNet

class GenericPagingSource<T>(
    private val mConnectionNet: ConnectionNet,
    private val mPageSize: Int = PAGINATION_PAGE_SIZE,
    private val getData: suspend (page: Int, pagination: Int) -> ResultNet<T>
) :
    PagingSource<Int, Any>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Any> {
        val page = params.key ?: 1
        var dataList: List<Any> = arrayListOf()
        return try {
            if (mConnectionNet.isNetworkConnected()) {
                when (val result = getData(page, mPageSize)) {
                    is ResultNet.Success -> {
                        (result.data as NewsModel).let { response ->
                            if (response.totalResults == 0) {
                                LoadResult.Page(emptyList(), null, null)
                            } else {
                                response.articles?.let {
                                    dataList = it
                                }

                                val to = page * PAGINATION_PAGE_SIZE
                                var total = 0
                                response.totalResults?.let {
                                    total = it
                                }
                                val nextKey = when {
                                    to < total -> page.plus(1)
                                    to == total -> null
                                    else -> null
                                }
                                val prevKey = if (page == 1) null else page - 1
                                LoadResult.Page(
                                    data = dataList,
                                    prevKey = prevKey,
                                    nextKey = nextKey
                                )
                            }
                        }
                    }

                    is ResultNet.ErrorApi -> {
                        LoadResult.Error(result.errorCls!!)
                    }

                    is ResultNet.ErrorException -> LoadResult.Error(
                        CheckErrorApiClass(
                            userMessage = result.message ?: ""
                        )
                    )

                    is ResultNet.ErrorNetwork -> LoadResult.Error(
                        CheckErrorApiClass(
                            userMessage = result.message ?: ""
                        )
                    )

                    is ResultNet.Loading -> LoadResult.Page(
                        data = emptyList(),
                        prevKey = null,
                        nextKey = null
                    )
                }
            } else {
                LoadResult.Error(
                    CheckErrorApiClass(
                        userMessage = ERROR_CHECK_NETWORK
                    )
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Any>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}