package com.example.domain.usecase

import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import com.example.domain.usecase.base.AbsFlowUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlin.coroutines.CoroutineContext

class ObserveRedditPostListUseCase(
    private val repository: RedditPostRepository,
    coroutineScope: CoroutineScope
): AbsFlowUseCase<List<RedditPost>, Unit>(coroutineScope) {

    override fun buildUseCase(params: Unit): Flow<List<RedditPost>> {
        return repository.observeRedditPostList()
    }
}