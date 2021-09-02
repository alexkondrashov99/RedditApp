package com.example.data.datasource.redditpostdataimpl

import android.content.Context
import com.example.data.datasource.models.AfterInfoData
import com.example.data.datasource.models.RedditPostData
import com.example.data.datasource.redditpostdatainterface.RedditPostDataLocalDataSource

//To storage reddit 'after' data
private const val sharedPreferences = "REDDIT_SHARED_PREFERENCES"
private const val afterSharedPrefs = "REDDIT_AFTER"


class RedditPostDataLocalDataSourceImpl(val context: Context): RedditPostDataLocalDataSource {



    override suspend fun getRedditPostCount(): Int {
        TODO("Realm???")
    }

    override suspend fun getRedditPostList(): List<RedditPostData> {
        TODO("Realm???")
    }

    override suspend fun refreshRedditPostList(list: List<RedditPostData>): List<RedditPostData> {
        TODO("Realm???")
    }

    override suspend fun updateRedditPostList(list: List<RedditPostData>): List<RedditPostData> {
        TODO("Realm???")

    }

    override suspend fun setRedditAfterInfo(afterInfoData: AfterInfoData) {
        val settings = context.getSharedPreferences(sharedPreferences,0)
        val editor = settings.edit()
        editor.putString(afterSharedPrefs, afterInfoData.after)
        editor.apply()
    }

    override suspend fun getRedditAfterInfo(): AfterInfoData? {
        val afterValue = context.getSharedPreferences(sharedPreferences, 0).getString(afterSharedPrefs,"") ?: ""
        if (afterValue.isEmpty()) {
            return null
        }
        else {
            return AfterInfoData(afterValue)
        }
    }

    override suspend fun clearRedditAfterInfo() {
        val settings = context.getSharedPreferences(sharedPreferences, 0)
        settings.edit().remove(afterSharedPrefs).commit()
    }
}