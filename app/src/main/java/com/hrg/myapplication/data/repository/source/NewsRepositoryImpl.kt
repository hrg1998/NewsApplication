package com.hrg.myapplication.data.repository.source

import com.hrg.myapplication.data.local.dao.NewsDao
import com.hrg.myapplication.data.remote.impl.NewsDataSource
import com.hrg.myapplication.data.repository.impl.NewsRepository
import com.hrg.myapplication.domain.models.Article
import com.hrg.myapplication.domain.models.NewsModel
import com.hrg.myapplication.domain.models.ResultNet
import com.hrg.myapplication.domain.models.SearchParametersModel
import com.hrg.myapplication.utils.networkHelper.CallBackNet
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsDao: NewsDao,
    private val newsDataSource: NewsDataSource
) :
    NewsRepository {
    override suspend fun getNews(searchParametersModel: SearchParametersModel): ResultNet<NewsModel> =
        CallBackNet.apiCall {
            val searchInResult = StringBuilder()
            for (i in searchParametersModel.searchIn.indices) {
                if (i != 0)
                    searchInResult.append(",")
                searchInResult.append(searchParametersModel.searchIn[i].name)
            }
            newsDataSource.getNews(
                query = searchParametersModel.q,
                page = searchParametersModel.page,
                pageSize = searchParametersModel.pageSize,
                sortBy = searchParametersModel.sortBy.name,
                searchIn = if (searchInResult.isEmpty()) null else searchInResult.toString(),
                from = searchParametersModel.from,
                to = searchParametersModel.to
            )
        }

    override fun getFavoriteNewsLive(): Flow<List<Article>> = newsDao.getArticlesLive()

    override fun saveFavoriteNews(article: Article) = newsDao.addFavoriteArticle(article)

    override fun removeFavorite(article: Article) = newsDao.remove(article)
}