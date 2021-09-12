package com.example.redditapp.di

import com.example.redditapp.RedditPostViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    //RedditPostActivity Activity
    viewModel { RedditPostViewModel() }
}