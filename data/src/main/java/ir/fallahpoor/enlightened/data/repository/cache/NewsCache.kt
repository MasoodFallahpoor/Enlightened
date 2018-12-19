package ir.fallahpoor.enlightened.data.repository.cache

import io.reactivex.Single
import ir.fallahpoor.enlightened.data.PreferencesManager
import ir.fallahpoor.enlightened.data.entity.NewsEntity
import ir.fallahpoor.enlightened.data.repository.dao.NewsDao
import javax.inject.Inject

class NewsCache @Inject
constructor(private val newsDao: NewsDao, private val preferencesManager: PreferencesManager) {

    fun isCacheEnabled() = preferencesManager.getBoolean("saveNews")

    fun getNews(pageNumber: Int, pageSize: Int): Single<List<NewsEntity>> {
        val offset = ((pageNumber - 1) * pageSize)
        return newsDao.getNews(pageSize, offset)
    }

    fun saveNews(newsList: List<NewsEntity>) {
        newsDao.saveNews(newsList)
    }

    fun deleteNews() {
        newsDao.deleteNews()
    }

}