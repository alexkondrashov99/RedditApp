package com.example.domain.usecase

import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import com.example.domain.usecase.base.AbsBaseUseCase
import kotlinx.coroutines.CoroutineScope


class GetLocalRedditTopListUseCase(
    private val repository: RedditPostRepository,
    coroutineScope: CoroutineScope
    ): AbsBaseUseCase<List<RedditPost>,Unit>(coroutineScope){

    override suspend fun buildUseCase(params: Unit): List<RedditPost> {
        return repository.getRedditPostList()
    }

}

