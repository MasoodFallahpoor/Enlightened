package ir.fallahpoor.enlightened.data.repository.datasource

import io.reactivex.Single
import ir.fallahpoor.enlightened.data.WebServiceFactory
import ir.fallahpoor.enlightened.data.entity.NewsEntity
import ir.fallahpoor.enlightened.data.entity.NewsListEntity
import ir.fallahpoor.enlightened.data.repository.cache.NewsCache
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

class CloudNewsDataSource @Inject
constructor(
    webServiceFactory: WebServiceFactory,
    private val newsCache: NewsCache
) : NewsDataSource {

    private val newsWebService = webServiceFactory.createService(NewsWebService::class.java)

    override fun getNews(
        country: String,
        category: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<NewsEntity>> {

        val newsObservable: Single<List<NewsEntity>> =
            newsWebService.getNews(country, category, pageNumber, pageSize, API_KEY)
                .map { newsListEntity: NewsListEntity -> newsListEntity.articles }

        return if (newsCache.isCacheEnabled()) {
            newsObservable
                .doOnSuccess { newsEntityList: List<NewsEntity> ->
                    cacheNews(pageNumber, newsEntityList)
                }
                .onErrorResumeNext { newsCache.getNews(pageNumber, pageSize) }
        } else {
            newsObservable
        }

    }

    private fun cacheNews(pageNumber: Int, newsListEntity: List<NewsEntity>) {
        if (pageNumber == 1) {
            newsCache.deleteNews()
        }
        newsCache.saveNews(newsListEntity)
    }

    override fun searchNews(
        searchQuery: String,
        pageNumber: Int,
        pageSize: Int
    ): Single<List<NewsEntity>> =
        newsWebService.searchNews(searchQuery, pageNumber, pageSize, API_KEY)
            .map { newsListEntity: NewsListEntity -> newsListEntity.articles }

    private interface NewsWebService {

        @GET("top-headlines")
        fun getNews(
            @Query("country") country: String,
            @Query("category") category: String,
            @Query("page") pageNumber: Int,
            @Query("pageSize") pageSize: Int,
            @Query("apiKey") apiKey: String
        ): Single<NewsListEntity>

        @GET("everything")
        fun searchNews(
            @Query("q") searchQuery: String,
            @Query("page") pageNumber: Int,
            @Query("pageSize") pageSize: Int,
            @Query("apiKey") apiKey: String
        ): Single<NewsListEntity>

    }

    private companion object {
        const val API_KEY = "a4eeb8db8ab64586b4262f4c7f906fa9"
    }

}