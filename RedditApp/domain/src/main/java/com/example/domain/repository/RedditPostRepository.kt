package com.example.domain.repository

import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import com.example.domain.models.REDDIT_T
import kotlinx.coroutines.flow.Flow

interface RedditPostRepository {

    /*
    * loading data from remote storage
    * deleting old data from local storage
    * saving new data to local storage
    */
    suspend fun fetchRemoteRedditPostList(limit: Int, t: REDDIT_T, count:Int, before: String, after: AfterInfo)
    /*
    * loading data from remote storage
    * saving new data to local storage
    */
    suspend fun updateRedditPostList(limit: Int, t: REDDIT_T, count:Int, before: String, after: AfterInfo)

    /* observers */
    fun observeRedditPostList(): Flow<List<RedditPost>>
    fun observeRedditAfterInfo(): Flow<AfterInfo>

    /* loading data from local storage */
    suspend fun getRedditAfterInfo(): AfterInfo

    /* loading data from local storage */
    suspend fun getRedditPostList(): List<RedditPost>
}

/*
RedditPostData loading scenario
1.First open app - loading data from local storage
2.Scrolling app - loading data from remote storage (fixed num of  records), saving in local storage
3.Pressing update button - loading data from data remote storage (fixed num of records), clearing storage, saving records to storage.
 */