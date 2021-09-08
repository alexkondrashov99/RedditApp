package com.example.domain.usecase

import com.example.domain.repository.RedditPostRepository

class LoadRedditTopList(private val repository: RedditPostRepository) {
    suspend fun execute() {
        repository.updateRedditPostList()
    }
}