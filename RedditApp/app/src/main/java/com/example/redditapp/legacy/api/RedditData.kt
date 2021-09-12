package com.example.redditapp.api

import android.graphics.Bitmap


data class RedditData (
    val title: String,
    val author: String,
    val createdUTC: Long,
    val numComments: Int,
    val url: String,
    val subreddit: String,
    val upvotes: Int,
    val thumbnailURL: String,
    val thumbnail: Bitmap?,
    val thumbnailPath: String
    )