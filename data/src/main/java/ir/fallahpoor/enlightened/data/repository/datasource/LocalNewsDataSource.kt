package ir.fallahpoor.enlightened.data.repository.datasource

import io.reactivex.Single
import ir.fallahpoor.enlightened.data.entity.NewsEntity
import ir.fallahpoor.enlightened.data.repository.cache.NewsCache
import javax.inject.Inject

class LocalNewsDataSource @Inject
constructor(private val newsCache: NewsCache) : NewsDataSource {

    override fun getNews(
        country: String,
        category: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<NewsEntity>> =
        newsCache.getNews(pageNumber, pageSize)

    override fun searchNews(
        searchQuery: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<NewsEntity>> =
        Single.error<List<NewsEntity>>(Throwable("Search not available when using LocalNewsDataSource"))

}