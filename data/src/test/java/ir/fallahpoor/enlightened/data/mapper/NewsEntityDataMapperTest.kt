package ir.fallahpoor.enlightened.data.mapper

import com.google.common.truth.Truth.assertThat
import ir.fallahpoor.enlightened.data.entity.NewsEntity
import ir.fallahpoor.enlightened.data.entity.SourceEntity
import ir.fallahpoor.enlightened.domain.model.News
import ir.fallahpoor.enlightened.domain.model.Source
import org.junit.Test

class NewsEntityDataMapperTest {

    @Test
    fun transform_should_transform_non_empty_list() {

        // Given
        val newsEntityDataMapper = NewsEntityDataMapper()

        // When
        val newsList: List<News> = newsEntityDataMapper.transform(getTestNewsEntityList())

        // Then
        assertThat(newsList).containsExactlyElementsIn(getTestNewsList()).inOrder()

    }

    @Test
    fun transform_should_transform_empty_list() {

        // Given
        val newsEntityDataMapper = NewsEntityDataMapper()

        // When
        val newsList: List<News> = newsEntityDataMapper.transform(emptyList())

        // Then
        assertThat(newsList).containsExactlyElementsIn(emptyList<News>())

    }

    private fun getTestNewsEntityList(): List<NewsEntity> {
        return listOf(
            NewsEntity(
                SourceEntity("id1", "source1"),
                "author1",
                "title1",
                "description1",
                "url1",
                "imageUrl1",
                "1-1-2019",
                "content1"
            ),
            NewsEntity(
                SourceEntity("id2", "source2"),
                "author2",
                "title2",
                "description2",
                "url2",
                "imageUrl2",
                "1-2-2019",
                "content2"
            ),
            NewsEntity(
                SourceEntity("id3", "source3"),
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

    private fun getTestNewsList(): List<News> {
        return listOf(
            News(
                Source("id1", "source1"),
                "author1",
                "title1",
                "description1",
                "url1",
                "imageUrl1",
                "1-1-2019",
                "content1"
            ),
            News(
                Source("id2", "source2"),
                "author2",
                "title2",
                "description2",
                "url2",
                "imageUrl2",
                "1-2-2019",
                "content2"
            ),
            News(
                Source("id3", "source3"),
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