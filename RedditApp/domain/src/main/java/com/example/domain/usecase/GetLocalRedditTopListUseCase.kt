package com.example.domain.usecase

import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository

class GetLocalRedditTopList(private val repository: RedditPostRepository) {
    suspend fun execute():List<RedditPost>{
        return repository.getRedditPostList()
    }
}