package com.example.redditapp


import android.content.Context
import com.example.data.datasource.redditpostdatasourceimpl.RedditPostLocalDataSourceImpl
import com.example.data.datasource.redditpostdatasourceimpl.RedditPostRemoteDataSourceImpl
import com.example.data.repository.RedditPostRepositoryImpl
import com.example.domain.repository.RedditPostRepository


interface ViewModelFactoryProvider {
    fun provideSomeViewModelFactory(): SomeViewModelFactory
}

private object DefaultViewModelProvider: ViewModelFactoryProvider {
    private fun redditPostLocalDataSource(): RedditPostLocalDataSourceImpl {
        return RedditPostLocalDataSourceImpl()
    }
    private fun redditPostRemoteDataSource() = RedditPostRemoteDataSourceImpl()

    private fun getSomeRepository(): RedditPostRepository {
        return RedditPostRepositoryImpl.getInstance(redditPostLocalDataSource(),redditPostRemoteDataSource())
    }

    override fun provideSomeViewModelFactory(): SomeViewModelFactory {
        val repository = getSomeRepository()
        return SomeViewModelFactory(repository)
    }
}

val Injector: ViewModelFactoryProvider
    get() = currentInjector

@Volatile private var currentInjector: ViewModelFactoryProvider = DefaultViewModelProvider

