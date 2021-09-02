package com.example.domain.usecase

import com.example.domain.models.AfterInfo
import com.example.domain.repository.RedditPostRepository

class SaveAfterInfo (private val repository: RedditPostRepository) {
    fun execute (afterInfo: AfterInfo) {
        //repository.setRedditAfterInfo(afterInfo)
    }
}