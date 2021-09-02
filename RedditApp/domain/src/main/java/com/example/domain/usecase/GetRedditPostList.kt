package com.example.domain.usecase

import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository

class GetRedditPostList (private val repository: RedditPostRepository) {
    fun execute(isRefresh: Boolean) /*: List<RedditPost> */{
        //repository.getRedditPostList(isRefresh)
    }
}