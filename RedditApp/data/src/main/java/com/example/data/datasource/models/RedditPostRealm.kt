package com.example.data.datasource.models

import com.example.domain.models.RedditPost
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class RedditPostRealm(
    @PrimaryKey var id: Int = 0,
    var title: String = "",
    var author: String = "",
    var createdUTC: Long = 0,
    var numComments: Int = 0,
    var url: String = "",
    var subreddit: String = "",
    var upvotes: Int = 0,
    var thumbnailURL: String = "",
): RealmObject()


fun RedditPost.mapToRealm(): RedditPostRealm{


    return RedditPostRealm(
        0,
        title,
        author,
        createdUTC,
        numComments,
        url,
        subreddit,
        upvotes,
        thumbnailURL,
    )
}
fun RedditPostRealm.mapToDomain(): RedditPost {
    return RedditPost(
        title,
        author,
        createdUTC,
        numComments,
        url,
        subreddit,
        upvotes,
        thumbnailURL,
    )
}