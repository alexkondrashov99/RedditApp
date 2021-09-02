package com.example.domain.repository

import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost

interface RedditPostRepository {

    suspend fun getRedditPostList(isRefresh: Boolean): List<RedditPost>

    suspend fun setRedditAfterInfo(after: AfterInfo)
    suspend fun getRedditAfterInfo(): AfterInfo?
}