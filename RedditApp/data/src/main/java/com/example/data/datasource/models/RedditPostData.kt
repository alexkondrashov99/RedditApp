package com.example.data.datasource.models

import android.graphics.Bitmap

data class RedditPostData (
    val title: String,
    val author: String,
    val createdUTC: Long,
    val numComments: Int,
    val url: String,
    val subreddit: String,
    val upvotes: Int,
    val thumbnailURL: String,
    val thumbnail: ByteArray?,
    val thumbnailPath: String
)