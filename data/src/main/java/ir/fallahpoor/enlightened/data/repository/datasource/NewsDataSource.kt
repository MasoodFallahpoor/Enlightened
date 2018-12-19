package ir.fallahpoor.enlightened.data.repository.datasource

import io.reactivex.Single
import ir.fallahpoor.enlightened.data.entity.NewsEntity

interface NewsDataSource {
    fun getNews(
        country: String,
        category: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<NewsEntity>>

    fun searchNews(
        searchQuery: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<NewsEntity>>
}