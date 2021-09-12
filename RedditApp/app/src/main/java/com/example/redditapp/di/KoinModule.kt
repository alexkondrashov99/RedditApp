package com.example.redditapp.di

import com.example.data.datasource.redditpostdatasourceimpl.RedditPostLocalDataSourceImpl
import com.example.data.datasource.redditpostdatasourceimpl.RedditPostRemoteDataSourceImpl
import com.example.data.datasource.redditpostdatasourceinterface.RedditPostLocalDataSource
import com.example.data.datasource.redditpostdatasourceinterface.RedditPostRemoteDataSource
import com.example.data.repository.RedditPostRepositoryImpl
import com.example.domain.repository.RedditPostRepository
import com.example.domain.usecase.*
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module

val koinModule = module {

    /**
    ██████╗░███████╗██████╗░░█████╗░░██████╗██╗████████╗░█████╗░██████╗░██╗███████╗░██████╗
    ██╔══██╗██╔════╝██╔══██╗██╔══██╗██╔════╝██║╚══██╔══╝██╔══██╗██╔══██╗██║██╔════╝██╔════╝
    ██████╔╝█████╗░░██████╔╝██║░░██║╚█████╗░██║░░░██║░░░██║░░██║██████╔╝██║█████╗░░╚█████╗░
    ██╔══██╗██╔══╝░░██╔═══╝░██║░░██║░╚═══██╗██║░░░██║░░░██║░░██║██╔══██╗██║██╔══╝░░░╚═══██╗
    ██║░░██║███████╗██║░░░░░╚█████╔╝██████╔╝██║░░░██║░░░╚█████╔╝██║░░██║██║███████╗██████╔╝
    ╚═╝░░╚═╝╚══════╝╚═╝░░░░░░╚════╝░╚═════╝░╚═╝░░░╚═╝░░░░╚════╝░╚═╝░░╚═╝╚═╝╚══════╝╚═════╝░
     **/
    single<RedditPostRepository> {
        RedditPostRepositoryImpl(
            get(),
            get()
        )
    }

    /**
    ██████╗░░█████╗░████████╗░█████╗░░░░░░░██████╗░█████╗░██╗░░░██╗██████╗░░█████╗░███████╗░██████╗
    ██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗░░░░░██╔════╝██╔══██╗██║░░░██║██╔══██╗██╔══██╗██╔════╝██╔════╝
    ██║░░██║███████║░░░██║░░░███████║░░░░░╚█████╗░██║░░██║██║░░░██║██████╔╝██║░░╚═╝█████╗░░╚█████╗░
    ██║░░██║██╔══██║░░░██║░░░██╔══██║░░░░░░╚═══██╗██║░░██║██║░░░██║██╔══██╗██║░░██╗██╔══╝░░░╚═══██╗
    ██████╔╝██║░░██║░░░██║░░░██║░░██║░░░░░██████╔╝╚█████╔╝╚██████╔╝██║░░██║╚█████╔╝███████╗██████╔╝
    ╚═════╝░╚═╝░░╚═╝░░░╚═╝░░░╚═╝░░╚═╝░░░░░╚═════╝░░╚════╝░░╚═════╝░╚═╝░░╚═╝░╚════╝░╚══════╝╚═════╝░
     **/
    //Remote
    single<RedditPostRemoteDataSource> { RedditPostRemoteDataSourceImpl() }
    //Local
    single<RedditPostLocalDataSource> { RedditPostLocalDataSourceImpl() }

//    //Use Cases
    //factory { GetLocalRedditTopListUseCase(get()) }
//    factory { LoadRedditTopListUseCase(get()) }
//    factory { ObserveRedditPostListUseCase(get()) }
//    factory { RefreshRedditPostListUseCase(get()) }
//    factory { ObserveAfterInfoUseCase(get()) }
    factory { (coroutineScope : CoroutineScope) -> GetLocalRedditTopListUseCase(get(),coroutineScope)}
    factory { (coroutineScope : CoroutineScope) -> LoadRedditTopListUseCase(get(),coroutineScope)}
    factory { (coroutineScope : CoroutineScope) -> ObserveAfterInfoUseCase(get(),coroutineScope)}
    factory { (coroutineScope : CoroutineScope) -> ObserveRedditPostListUseCase(get(),coroutineScope)}
    factory { (coroutineScope : CoroutineScope) -> RefreshRedditPostListUseCase(get(),coroutineScope)}


}