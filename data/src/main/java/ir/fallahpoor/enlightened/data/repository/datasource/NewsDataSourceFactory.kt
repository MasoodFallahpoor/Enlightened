package ir.fallahpoor.enlightened.data.repository.datasource

import javax.inject.Inject

class NewsDataSourceFactory @Inject
internal constructor(private val cloudNewsDataStore: CloudNewsDataSource) {
    fun create(): NewsDataSource = cloudNewsDataStore
}