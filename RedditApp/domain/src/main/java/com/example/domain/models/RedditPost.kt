package com.example.domain.models

data class RedditPost (
    val title: String,
    val author: String,
    val createdUTC: Long,
    val numComments: Int,
    val url: String,
    val subreddit: String,
    val upvotes: Int,
    val thumbnailURL: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RedditPost

        if (title != other.title) return false
        if (author != other.author) return false
        if (createdUTC != other.createdUTC) return false
        if (numComments != other.numComments) return false
        if (url != other.url) return false
        if (subreddit != other.subreddit) return false
        if (upvotes != other.upvotes) return false
        if (thumbnailURL != other.thumbnailURL) return false

        return true
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + author.hashCode()
        result = 31 * result + createdUTC.hashCode()
        result = 31 * result + numComments
        result = 31 * result + url.hashCode()
        result = 31 * result + subreddit.hashCode()
        result = 31 * result + upvotes
        result = 31 * result + thumbnailURL.hashCode()
        return result
    }
}