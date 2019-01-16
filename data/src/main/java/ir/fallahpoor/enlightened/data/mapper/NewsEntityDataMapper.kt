package ir.fallahpoor.enlightened.data.mapper

import ir.fallahpoor.enlightened.data.entity.NewsEntity
import ir.fallahpoor.enlightened.data.entity.SourceEntity
import ir.fallahpoor.enlightened.domain.model.News
import ir.fallahpoor.enlightened.domain.model.Source
import javax.inject.Inject

class NewsEntityDataMapper @Inject
constructor() {

    fun transform(newsEntities: List<NewsEntity>): List<News> =
        newsEntities.map { transformNewsEntityToNews(it) }

    private fun transformNewsEntityToNews(newsEntity: NewsEntity) =
        with(newsEntity) {
            News(
                transformSourceEntityToSource(source),
                author,
                title,
                description,
                url,
                urlToImage,
                publishedAt,
                content
            )
        }

    private fun transformSourceEntityToSource(sourceEntity: SourceEntity) =
        Source(sourceEntity.id, sourceEntity.name)

}