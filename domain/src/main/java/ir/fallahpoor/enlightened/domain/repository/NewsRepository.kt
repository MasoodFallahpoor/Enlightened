package ir.fallahpoor.enlightened.domain.repository

import io.reactivex.Completable
import io.reactivex.Single
import ir.fallahpoor.enlightened.domain.model.News

interface NewsRepository {

    fun getNews(
        country: String,
        category: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<News>>

    fun searchNews(
        searchQuery: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<News>>

    fun deleteNews(): Completable

}