package com.hrg.myapplication.domain.models

import android.os.Parcelable
import com.hrg.myapplication.utils.SearchIn
import com.hrg.myapplication.utils.SortBy
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchParametersModel(
    var q: String,
    var page: Int = 1,
    var pageSize: Int = 10,
    var sortBy: SortBy = SortBy.PublishedAt,
    var searchIn: ArrayList<SearchIn> = ArrayList(),
    var from: String? = null,
    var to: String? = null
) : Parcelable {
    fun existFilter(): Boolean {
        return sortBy != SortBy.PublishedAt || searchIn.isNotEmpty()
    }

    fun clone() = SearchParametersModel(q, page, pageSize, sortBy, searchIn.clone() as ArrayList<SearchIn>, from, to)
}

sealed class ResultNet<T> {

    class Loading<T> : ResultNet<T>()

    data class Success<T>(val data: T) : ResultNet<T>()

    data class ErrorNetwork<T>(val message: String?) : ResultNet<T>()

    data class ErrorException<T>(val message: String?) : ResultNet<T>()

    data class ErrorApi<T>(val errorCls: CheckErrorApiClass?) : ResultNet<T>()
}

data class CheckErrorApiClass(
    var code: Int? = -1,
    var userMessage: String = "",
    val errorModel: NewsErrorModel? = null
) : Throwable()