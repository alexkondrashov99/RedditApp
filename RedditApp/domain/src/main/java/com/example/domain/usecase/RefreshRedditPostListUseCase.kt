package com.example.domain.usecase

import com.example.domain.models.AfterInfo
import com.example.domain.models.REDDIT_T
import com.example.domain.repository.RedditPostRepository
import com.example.domain.usecase.base.AbsBaseUseCase
import kotlinx.coroutines.CoroutineScope

class RefreshRedditPostListUseCase(
    private val repository: RedditPostRepository,
    coroutineScope: CoroutineScope
): AbsBaseUseCase<Unit, RefreshRedditPostListUseCase.Params>(coroutineScope) {

    override suspend fun buildUseCase(params: Params) {
        return repository.fetchRemoteRedditPostList(
            params.limit,
            params.t,
            params.count,
            params.before,
            params.after
        )
    }

    class Params(
        val limit: Int,
        val t: REDDIT_T,
        val count: Int = 0,
        val before: String = "",
        val after: AfterInfo = AfterInfo("")
    )
}