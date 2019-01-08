package ir.fallahpoor.enlightened.presentation.newslist.model

import com.google.common.truth.Truth
import ir.fallahpoor.enlightened.domain.model.News
import ir.fallahpoor.enlightened.domain.model.Source
import org.junit.Test

class NewsListDataMapperTest {

    @Test
    fun transform_should_transform_non_empty_list() {

        // Given
        val newsListDataMapper = NewsListDataMapper()

        // When
        val newsModelList: List<NewsModel> = newsListDataMapper.transform(getTestNewsList())

        // Then
        Truth.assertThat(newsModelList).containsExactlyElementsIn(getTestNewsModelList()).inOrder()

    }

    @Test
    fun transform_should_transform_empty_list() {

        // Given
        val newsListDataMapper = NewsListDataMapper()

        // When
        val newsModelList: List<NewsModel> = newsListDataMapper.transform(emptyList())

        // Then
        Truth.assertThat(newsModelList).containsExactlyElementsIn(emptyList<NewsModel>())

    }

    private fun getTestNewsList(): List<News> {
        return listOf(
            News(
                Source("id1", "source1"),
                "author1",
                "title1 - source1",
                "description1",
                "url1",
                "imageUrl1",
                "1-1-2019",
                "content1"
            ),
            News(
                Source("id2", "source2"),
                "author2",
                "title2 | source2",
                "description2",
                "url2",
                "imageUrl2",
                "1-2-2019",
                "content2"
            ),
            News(
                Source("id3", "source3"),
                "author3",
                "title3 - source3",
                "description3",
                "url3",
                "imageUrl3",
                "1-3-2019",
                "content3"
            )
        )
    }

    private fun getTestNewsModelList(): List<NewsModel> {
        return listOf(
            NewsModel(
                SourceModel("id1", "source1"),
                "author1",
                "title1",
                "description1",
                "url1",
                "imageUrl1",
                "1-1-2019",
                "content1"
            ),
            NewsModel(
                SourceModel("id2", "source2"),
                "author2",
                "title2",
                "description2",
                "url2",
                "imageUrl2",
                "1-2-2019",
                "content2"
            ),
            NewsModel(
                SourceModel("id3", "source3"),
                "author3",
                "title3",
                "description3",
                "url3",
                "imageUrl3",
                "1-3-2019",
                "content3"
            )
        )
    }

}