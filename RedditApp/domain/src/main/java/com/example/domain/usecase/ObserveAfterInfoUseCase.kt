package com.example.domain.usecase

import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import kotlinx.coroutines.flow.Flow

class ObserveAfterInfo(private val repository: RedditPostRepository) {
    suspend fun execute(): Flow<AfterInfo> {
        return repository.observeRedditAfterInfo()
    }
}