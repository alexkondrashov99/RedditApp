package com.example.domain.usecase

import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import kotlinx.coroutines.flow.Flow

class ObserveRedditPostList(private val repository: RedditPostRepository) {
    suspend fun execute(): Flow<List<RedditPost>> {
        return repository.observeRedditPostList()
    }
}