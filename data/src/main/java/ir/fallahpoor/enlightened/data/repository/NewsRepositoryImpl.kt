package ir.fallahpoor.enlightened.data.repository

import io.reactivex.Completable
import io.reactivex.Single
import ir.fallahpoor.enlightened.data.mapper.NewsEntityDataMapper
import ir.fallahpoor.enlightened.data.repository.dao.NewsDao
import ir.fallahpoor.enlightened.data.repository.datasource.NewsDataSourceFactory
import ir.fallahpoor.enlightened.domain.model.News
import ir.fallahpoor.enlightened.domain.repository.NewsRepository

class NewsRepositoryImpl(
    private val newsDao: NewsDao,
    private val newsDataSourceFactory: NewsDataSourceFactory,
    private val newsEntityDataMapper: NewsEntityDataMapper
) : NewsRepository {

    override fun getNews(
        country: String,
        category: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<News>> =
        newsDataSourceFactory.create()
            .getNews(country, category, pageNumber, pageSize)
            .map(newsEntityDataMapper::transform)

    override fun searchNews(
        searchQuery: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<News>> =
        newsDataSourceFactory.create()
            .searchNews(searchQuery, pageNumber, pageSize)
            .map(newsEntityDataMapper::transform)

    override fun deleteNews(): Completable =
        Completable.create {
            newsDao.deleteNews()
            it.onComplete()
        }

}