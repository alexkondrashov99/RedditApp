package com.example.data.datasource.models

import android.graphics.Bitmap
import com.example.domain.models.RedditPost
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

data class RedditPostData(
    var title: String = "",
    var author: String = "",
    var createdUTC: Long = 0,
    var numComments: Int = 0,
    @PrimaryKey var url: String = "",
    var subreddit: String = "",
    var upvotes: Int = 0,
    var thumbnailURL: String = "",
    var thumbnail: RealmList<Byte>? = null,
    var thumbnailPath: String = ""
): RealmObject()


fun RedditPost.mapToRealm(): RedditPostData{

    val thumbnailByteList = RealmList<Byte>()
    thumbnail?.forEach {
        it?.run { thumbnailByteList.add(this)}
    }
    return RedditPostData(
        title,
        author,
        createdUTC,
        numComments,
        url,
        subreddit,
        upvotes,
        thumbnailURL,
        thumbnailByteList,
        thumbnailPath
    )
}
fun RedditPostData.mapToDomain(): RedditPost {
    return RedditPost(
        title,
        author,
        createdUTC,
        numComments,
        url,
        subreddit,
        upvotes,
        thumbnailURL,
        thumbnail?.toByteArray(),
        thumbnailPath
    )
}