package com.example.domain.usecase

import com.example.domain.models.AfterInfo
import com.example.domain.repository.RedditPostRepository

class GetAfterInfo (private val repository: RedditPostRepository) {
    fun execute() /*AfterInfo?*/{
        //repository.getRedditAfterInfo()
    }
}