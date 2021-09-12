package com.example.redditapp

import android.app.Application
import com.example.redditapp.di.koinModule
import com.example.redditapp.di.viewModelModule
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class RedditApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)

        startKoin{
            androidLogger()
            androidContext(this@RedditApplication)
            modules(listOf(koinModule, viewModelModule))
        }
    }
}