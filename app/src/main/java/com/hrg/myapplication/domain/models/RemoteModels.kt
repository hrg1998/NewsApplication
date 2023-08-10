package com.hrg.myapplication.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

data class NewsModel(
    val articles: List<Article>?,
    val status: String?,
    val totalResults: Int?
)

@Entity
@Parcelize
data class Article(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String?,
    val source: Source?,
    val title: String?,
    val url: String?,
    val urlToImage: String?
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (other is Article) {
            return author == other.author
                    && content == other.content
                    && description == other.description
                    && publishedAt == other.publishedAt
                    && source == other.source
                    && title == other.title
                    && url == other.url
                    && urlToImage == other.urlToImage
        }
        return super.equals(other)
    }
}

@Parcelize
data class Source(
    val id: String?,
    val name: String?
) : Parcelable {
    override fun toString(): String = "$id,$name"
    override fun equals(other: Any?): Boolean {
        if (other is Source) {
            return name == other.name && id == other.id
        } else {
            return super.equals(other)
        }
    }
}