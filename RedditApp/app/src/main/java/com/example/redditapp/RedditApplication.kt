package com.example.redditapp

import android.app.Application
import io.realm.Realm

class RedditApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}