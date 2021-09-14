package com.example.redditapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.domain.models.AfterInfo
import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import com.example.domain.usecase.*
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Rule
import org.junit.rules.TestRule


class RedditPostViewModelTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()



    @Mock
    private lateinit var repository: RedditPostRepository

    private lateinit var testRedditPost: RedditPost

    /** VIEWMODEL **/
    private lateinit var viewModel: RedditPostViewModel

    /** USECASES **/
    private lateinit var getLocalRedditTopListUseCase: GetLocalRedditTopListUseCase
    private lateinit var loadRedditTopListUseCase: LoadRedditTopListUseCase
    private lateinit var observeAfterInfoUseCase: ObserveAfterInfoUseCase
    private lateinit var observeRedditTopListUseCase: ObserveRedditPostListUseCase
    private lateinit var refreshRedditPostListUseCase: RefreshRedditPostListUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        testRedditPost = RedditPost(
            "testTitle",
            "testAuthor",
            1592410647,
            9999,
            "/r/pics/comments/haucpf/ive_found_a_few_funny_memories_during_lockdown/",
            "r/wallstreetbets",
            25909,
            "https://b.thumbs.redditmedia.com/3bOsV7-uLwCGOo37i3CeSG4n3XBD7DI11sIdl3q97YY.jpg")

        observeAfterInfoUseCase = ObserveAfterInfoUseCase(repository)
        getLocalRedditTopListUseCase = GetLocalRedditTopListUseCase(repository)
        loadRedditTopListUseCase = LoadRedditTopListUseCase(repository)
        observeRedditTopListUseCase = ObserveRedditPostListUseCase(repository)
        refreshRedditPostListUseCase = RefreshRedditPostListUseCase(repository)
        viewModel = RedditPostViewModel(
            getLocalRedditTopListUseCase,
            loadRedditTopListUseCase,
            observeAfterInfoUseCase,
            observeRedditTopListUseCase,
            refreshRedditPostListUseCase,
        )
    }

    @Test
    fun `check if afterInfo livedata works properly`() = testCoroutineRule.runBlockingTest {
        whenever(repository.observeRedditAfterInfo()).thenReturn(
            flow {
                    emit(AfterInfo("kek"))
            }
        )
        viewModel.init()
        assertThat(viewModel.afterInfo.value?.after == "kek").isTrue()
    }
    @Test
    fun `check if redditPost livedata works properly`() = testCoroutineRule.runBlockingTest {
        whenever(repository.observeRedditPostList()).thenReturn(
            flow {
                emit(
                    listOf(testRedditPost)
                )
            }
        )
        viewModel.init()
        assertThat(viewModel.redditPostList.value?.get(0) == testRedditPost).isTrue()
    }
}