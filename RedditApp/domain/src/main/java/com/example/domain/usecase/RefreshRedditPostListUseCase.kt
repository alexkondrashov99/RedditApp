package com.example.domain.usecase

import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import kotlinx.coroutines.flow.Flow

class RefreshRedditPostList(private val repository: RedditPostRepository) {
    suspend fun execute() {
        repository.fetchRemoteRedditPostList()
    }
}