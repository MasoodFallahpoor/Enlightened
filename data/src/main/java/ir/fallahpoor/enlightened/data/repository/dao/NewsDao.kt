package ir.fallahpoor.enlightened.data.repository.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Single
import ir.fallahpoor.enlightened.data.entity.NewsEntity

@Dao
interface NewsDao {

    @Query("SELECT * FROM news ORDER BY  publishedAt DESC LIMIT :limit OFFSET :offset")
    fun getNews(limit: Int, offset: Int): Single<List<NewsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveNews(newsList: List<NewsEntity>)

    @Query("DELETE FROM news")
    fun deleteNews()

    @Query("SELECT COUNT(*) FROM news")
    fun getNumberOfNews(): Int

}