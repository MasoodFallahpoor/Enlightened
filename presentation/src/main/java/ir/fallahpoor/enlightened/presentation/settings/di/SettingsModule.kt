package ir.fallahpoor.enlightened.presentation.settings.di

import android.content.Context
import dagger.Module
import dagger.Provides
import ir.fallahpoor.enlightened.data.mapper.NewsEntityDataMapper
import ir.fallahpoor.enlightened.data.repository.Database
import ir.fallahpoor.enlightened.data.repository.NewsRepositoryImpl
import ir.fallahpoor.enlightened.data.repository.dao.NewsDao
import ir.fallahpoor.enlightened.data.repository.datasource.NewsDataSourceFactory
import ir.fallahpoor.enlightened.domain.repository.NewsRepository

@Module
class SettingsModule {

    @Provides
    internal fun provideNewsRepository(
        newsDao: NewsDao,
        newsDataSourceFactory: NewsDataSourceFactory,
        newsEntityDataMapper: NewsEntityDataMapper
    ): NewsRepository = NewsRepositoryImpl(newsDao, newsDataSourceFactory, newsEntityDataMapper)

    @Provides
    internal fun provideNewsDao(context: Context) = Database.getDatabase(context).newsDao()

}