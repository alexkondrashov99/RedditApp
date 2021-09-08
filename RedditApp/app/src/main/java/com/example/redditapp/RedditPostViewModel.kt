package com.example.redditapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.AfterInfo
import com.example.domain.models.REDDIT_T
import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import com.example.domain.usecase.*
import com.example.domain.usecase.base.Request
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

class RedditPostViewModel (private val repository: RedditPostRepository): ViewModel() {

    private val fetchingDataSize = 10
    private val maxDataSize = 50
    private val fetchingDataTime = REDDIT_T.ALL
    private val fetchingCount = 0
    private val fetchingBefore = ""
    private var isLoading = false
    /** ------------------------------------------------------------------------------------------------- **/

    /**      UseCases       **/
    private val getLocalRedditTopListUseCase = GetLocalRedditTopListUseCase(repository,viewModelScope)
    private val loadRedditTopListUseCase = LoadRedditTopListUseCase(repository,viewModelScope)
    private val observeAfterInfoUseCase = ObserveAfterInfoUseCase(repository,viewModelScope)
    private val observeRedditTopListUseCase = ObserveRedditPostListUseCase(repository,viewModelScope)
    private val refreshRedditPostListUseCase = RefreshRedditPostListUseCase(repository,viewModelScope)

    /**      LiveData       **/
    private val _listViewVisibility = MutableLiveData<Boolean>(false)
    public val listViewVisibility: LiveData<Boolean>
        get() = _listViewVisibility

    private val _progressBarVisibility = MutableLiveData<Boolean>(false)
    public val progressBarVisibility: LiveData<Boolean>
        get() = _progressBarVisibility

    private val _redditPostList = MutableLiveData<List<RedditPost>>(listOf())
    public val redditPostList: LiveData<List<RedditPost>>
        get() = _redditPostList

    private val _afterInfo = MutableLiveData<AfterInfo>(AfterInfo(""))
    public val afterInfo: LiveData<AfterInfo>
        get() = _afterInfo


    init {
        observeRedditPostList()
        observeAfterInfo()
    }

    private fun observeAfterInfo(){
        val request = Request<AfterInfo>().apply {
            onComplete = { aInfoList ->
                _afterInfo.value = aInfoList
            }
            onError = {
                var message = it.message
                //TODO(Some SnackBar manipulations...)
            }
        }
        observeAfterInfoUseCase.execute(request,Unit)
    }
    private fun observeRedditPostList(){

        val request = Request<List<RedditPost>>().apply {
            onComplete = { postList ->
                _redditPostList.value = postList
                isLoading = false
                _progressBarVisibility.value = false
            }
            onError = {
                //TODO(Some SnackBar manipulations...)
            }
        }
        observeRedditTopListUseCase.execute(request,Unit)

    }
    fun loadRedditTopList() {
        if(isLoading == false) {//if (isLoading == false) {
            isLoading = true
            _progressBarVisibility.value = true
            //_listViewVisibility
            loadRedditTopListUseCase.execute(
                LoadRedditTopListUseCase.Params(
                    limit = fetchingDataSize,
                    t = fetchingDataTime,
                    after = _afterInfo.value ?: AfterInfo("")
                )
            )
        }
    }
    fun refreshRedditPostList() {
        if(isLoading == false) {
            isLoading = true
            _afterInfo.value = AfterInfo("")
            _progressBarVisibility.value = true
            refreshRedditPostListUseCase.execute(
                RefreshRedditPostListUseCase.Params(
                    limit = fetchingDataSize,
                    t = fetchingDataTime,
                    after = AfterInfo("")
                )
            )
        }
    }

}






