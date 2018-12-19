package ir.fallahpoor.enlightened.data.entity

data class NewsListEntity(val status: String, val totalResults: Int, val articles: List<NewsEntity>)