package ir.fallahpoor.enlightened.presentation.newslist.model

import ir.fallahpoor.enlightened.domain.model.News
import ir.fallahpoor.enlightened.domain.model.Source
import javax.inject.Inject

class NewsListDataMapper @Inject
constructor() {

    fun transform(newsList: List<News>): List<NewsModel> =
        newsList.map { transformNewsToNewsModel(it) }

    private fun transformNewsToNewsModel(news: News) =
        with(news) {
            NewsModel(
                transformSourceToSourceModel(source),
                author,
                removeNewsSourceFromNewsTitle(title),
                description,
                url,
                urlToImage,
                publishedAt,
                content
            )
        }

    private fun transformSourceToSourceModel(source: Source) = SourceModel(source.id, source.name)

    private fun removeNewsSourceFromNewsTitle(title: String): String {
        val indexOfSeparator = title.indexOfLast { it == '-' || it == '|' }
        return if (indexOfSeparator != -1) {
            title.substring(0, indexOfSeparator).trim()
        } else {
            title
        }
    }

}