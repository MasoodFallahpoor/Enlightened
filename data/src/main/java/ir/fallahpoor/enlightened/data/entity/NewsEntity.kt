package ir.fallahpoor.enlightened.data.entity

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news")
data class NewsEntity(
    @Embedded
    val source: SourceEntity,
    @Nullable
    val author: String?,
    @PrimaryKey
    @NonNull
    val title: String,
    @Nullable
    val description: String?,
    val url: String,
    @Nullable
    val urlToImage: String?,
    val publishedAt: String,
    @Nullable
    val content: String?
)