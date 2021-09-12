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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlin.random.Random

class RedditPostViewModel (): ViewModel(), KoinComponent{

    private val fetchingDataSize = 10
    private val maxDataSize = 50
    private val fetchingDataTime = REDDIT_T.YEAR
    private val fetchingCount = 0
    private val fetchingBefore = ""
    private var isLoading = false
    /** ------------------------------------------------------------------------------------------------- **/
    /**      UseCases       **/
    private val getLocalRedditTopListUseCase:GetLocalRedditTopListUseCase by inject{
        parametersOf(viewModelScope)
    }
    private val loadRedditTopListUseCase:LoadRedditTopListUseCase by inject{
        parametersOf(viewModelScope)
    }
    private val observeAfterInfoUseCase:ObserveAfterInfoUseCase by inject{
        parametersOf(viewModelScope)
    }
    private val observeRedditTopListUseCase:ObserveRedditPostListUseCase by inject{
        parametersOf(viewModelScope)
    }
    private val refreshRedditPostListUseCase:RefreshRedditPostListUseCase by inject{
        parametersOf(viewModelScope)
    }


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

    private val _errorMessage = MutableLiveData<String?>(null)
    public val errorMessage: LiveData<String?>
        get() = _errorMessage


    init {

        //TODO(Asynchronous observing PROPERLY)
        _progressBarVisibility.value = true
        observeRedditPostList()
        observeAfterInfo()

    }

    private fun observeAfterInfo(){
        val request = Request<AfterInfo>().apply {
            onComplete = { aInfoList ->
                _afterInfo.value = aInfoList
            }
            onError = {
                _errorMessage.value = it.message
            }
            onTerminate = {
                _progressBarVisibility.value = false
                _listViewVisibility.value = true
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
                _listViewVisibility.value = true
            }
            onError = {
                _errorMessage.value = it.message
            }
            onTerminate = {
                _progressBarVisibility.value = false
                _listViewVisibility.value = true
            }
        }
        observeRedditTopListUseCase.execute(request,Unit)

    }
    fun loadRedditTopList() {
        if(isLoading == false) {
            _afterInfo.value?.let { afterInfo ->
                if(afterInfo.after == "")
                        return
                isLoading = true
                _progressBarVisibility.value = true

                loadRedditTopListUseCase.execute(
                    LoadRedditTopListUseCase.Params(
                        limit = fetchingDataSize,
                        t = fetchingDataTime,
                        after = afterInfo
                    ),
                    Request<Unit>().apply {
                        onError = {
                            _errorMessage.value = it.message
                        }
                        onTerminate = {
                            _progressBarVisibility.value = false
                            isLoading = false
                        }
                    }
                )
            }
        }

    }
    fun refreshRedditPostList() {
        if(isLoading == false) {
            isLoading = true

            _progressBarVisibility.value = true
            _listViewVisibility.value = false

            refreshRedditPostListUseCase.execute(
                RefreshRedditPostListUseCase.Params(
                    limit = fetchingDataSize,
                    t = fetchingDataTime,
                    after = AfterInfo("")
                ),
                Request<Unit>().apply {
                    onError = {
                        _errorMessage.value = it.message
                    }
                    onTerminate = {
                        _progressBarVisibility.value = false
                        isLoading = false
                        _listViewVisibility.value = true

                    }
                }
            )
        }
    }

}






