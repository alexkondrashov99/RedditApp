package com.example.data.repository


import com.example.data.datasource.models.AfterInfoData
import com.example.data.datasource.models.RedditPostData
import com.example.data.datasource.redditpostdatainterface.RedditPostDataLocalDataSource
import com.example.data.datasource.redditpostdatainterface.RedditPostDataRemoteDataSource
import com.example.data.datasource.redditpostdatainterface.RedditPostDataRemoteDataSource.REDDIT_T
import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository

class RedditPostRepositoryImpl (
    private val localDataSource: RedditPostDataLocalDataSource,
    private val remoteDataSource: RedditPostDataRemoteDataSource
) : RedditPostRepository {

    private val fetchingDataSize = 10
    private val fetchingDataTime = REDDIT_T.HOUR
    private val fetchingCount = 0
    private val fetchingBefore = ""

    override suspend fun getRedditPostList(isRefresh: Boolean): List<RedditPost> {
        var redditPostDataList: List<RedditPostData>
        if(isRefresh) {
            val fetchingAfterInfo = getRedditAfterInfo() ?: AfterInfo("")

            //map AfterInfo to AfterInfoData
            val fetchingAfterInfoData = fetchingAfterInfo.mapToData()

            redditPostDataList = remoteDataSource.fetchRedditPostList(
                limit = fetchingDataSize,
                t = fetchingDataTime,
                count = fetchingCount,
                before = fetchingBefore,
                after = fetchingAfterInfoData
            )

            //saving fetched data to local database, saving bitmaps to /images
            redditPostDataList = localDataSource.refreshRedditPostList(redditPostDataList)
            return redditPostDataList.map { it.mapToDomain() }
        }
        else {
            redditPostDataList = localDataSource.getRedditPostList()
            return redditPostDataList.map { it.mapToDomain() }
        }
    }

    override suspend fun setRedditAfterInfo(after: AfterInfo) {
        TODO("Not yet implemented")
    }

    override suspend fun getRedditAfterInfo(): AfterInfo? {
        TODO("Not yet implemented")
    }
}
fun RedditPostData.mapToDomain(): RedditPost{
    return RedditPost(
        title,
        author,
        createdUTC,
        numComments,
        url,
        subreddit,
        upvotes,
        thumbnailURL,
        thumbnail,
        thumbnailPath
    )
}
fun RedditPost.mapToData(): RedditPostData{
    return RedditPostData(
        title,
        author,
        createdUTC,
        numComments,
        url,
        subreddit,
        upvotes,
        thumbnailURL,
        thumbnail,
        thumbnailPath
    )
}

fun AfterInfo.mapToData():AfterInfoData{
    return AfterInfoData(after)
}
fun AfterInfoData.mapToDomain():AfterInfo{
    return AfterInfo(after)
}