package com.example.redditapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.RedditPost
import com.example.domain.repository.RedditPostRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RedditPostActivity: AppCompatActivity() {

    /*
    ██████╗░███████╗██████╗░██████╗░██╗████████╗██████╗░░█████╗░░██████╗████████╗██╗░░░░░██╗░██████╗████████╗
    ██╔══██╗██╔════╝██╔══██╗██╔══██╗██║╚══██╔══╝██╔══██╗██╔══██╗██╔════╝╚══██╔══╝██║░░░░░██║██╔════╝╚══██╔══╝
    ██████╔╝█████╗░░██║░░██║██║░░██║██║░░░██║░░░██████╔╝██║░░██║╚█████╗░░░░██║░░░██║░░░░░██║╚█████╗░░░░██║░░░
    ██╔══██╗██╔══╝░░██║░░██║██║░░██║██║░░░██║░░░██╔═══╝░██║░░██║░╚═══██╗░░░██║░░░██║░░░░░██║░╚═══██╗░░░██║░░░
    ██║░░██║███████╗██████╔╝██████╔╝██║░░░██║░░░██║░░░░░╚█████╔╝██████╔╝░░░██║░░░███████╗██║██████╔╝░░░██║░░░
    ╚═╝░░╚═╝╚══════╝╚═════╝░╚═════╝░╚═╝░░░╚═╝░░░╚═╝░░░░░░╚════╝░╚═════╝░░░░╚═╝░░░╚══════╝╚═╝╚═════╝░░░░╚═╝░░░
    */
    private var redditPostList:List<RedditPost> = ArrayList<RedditPost>()
    /*
    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    █████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗
    ╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝
    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░ */


    /*
    ██╗░░░██╗██╗███████╗░██╗░░░░░░░██╗███╗░░░███╗░█████╗░██████╗░███████╗██╗░░░░░
    ██║░░░██║██║██╔════╝░██║░░██╗░░██║████╗░████║██╔══██╗██╔══██╗██╔════╝██║░░░░░
    ╚██╗░██╔╝██║█████╗░░░╚██╗████╗██╔╝██╔████╔██║██║░░██║██║░░██║█████╗░░██║░░░░░
    ░╚████╔╝░██║██╔══╝░░░░████╔═████║░██║╚██╔╝██║██║░░██║██║░░██║██╔══╝░░██║░░░░░
    ░░╚██╔╝░░██║███████╗░░╚██╔╝░╚██╔╝░██║░╚═╝░██║╚█████╔╝██████╔╝███████╗███████╗
    ░░░╚═╝░░░╚═╝╚══════╝░░░╚═╝░░░╚═╝░░╚═╝░░░░░╚═╝░╚════╝░╚═════╝░╚══════╝╚══════╝
     */
    private val viewModel:RedditPostViewModel =
        Injector
            .provideSomeViewModelFactory()
            .create(RedditPostViewModel::class.java)
    /*
    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    █████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗
    ╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝
    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/



    /*
    ██╗░░░░░░█████╗░███████╗██╗░░░██╗░░░██╗░░░██╗██╗███████╗░██╗░░░░░░░██╗░██████╗
    ██║░░░░░██╔══██╗╚════██║╚██╗░██╔╝░░░██║░░░██║██║██╔════╝░██║░░██╗░░██║██╔════╝
    ██║░░░░░███████║░░███╔═╝░╚████╔╝░░░░╚██╗░██╔╝██║█████╗░░░╚██╗████╗██╔╝╚█████╗░
    ██║░░░░░██╔══██║██╔══╝░░░░╚██╔╝░░░░░░╚████╔╝░██║██╔══╝░░░░████╔═████║░░╚═══██╗
    ███████╗██║░░██║███████╗░░░██║░░░░░░░░╚██╔╝░░██║███████╗░░╚██╔╝░╚██╔╝░██████╔╝
    ╚══════╝╚═╝░░╚═╝╚══════╝░░░╚═╝░░░░░░░░░╚═╝░░░╚═╝╚══════╝░░░╚═╝░░░╚═╝░░╚═════╝░
     */
    private val progressBar:ProgressBar by lazy {findViewById(R.id.progressBar)}
    private val recyclerView:RecyclerView by lazy {findViewById(R.id.recyclerView)}
    private val nestedScrollView:NestedScrollView by lazy {findViewById(R.id.nestedScrollView)}
    private val floatingButton:FloatingActionButton by lazy {findViewById(R.id.fab)}
    /*
    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    █████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗
    ╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝
    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

     private var redditAdapter:RedditAdapter = RedditAdapter(emptyArray())


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.redditPostList.observe(this){ dataList ->
            redditPostList = dataList
            redditAdapter = RedditAdapter(redditPostList.toTypedArray())
            recyclerView.adapter = redditAdapter // TODO (Notify)
            recyclerView.visibility = View.VISIBLE
        }

        viewModel.progressBarVisibility.observe(this){ isVisible ->
            if(isVisible)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        }
        viewModel.listViewVisibility.observe(this){ isVisible ->
            if(isVisible)
                recyclerView.visibility = View.VISIBLE
            else
                recyclerView.visibility = View.GONE
        }

        progressBar.visibility = View.GONE
        recyclerView.layoutManager = LinearLayoutManager(this)

        nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                if(scrollY>= v.getChildAt(0).measuredHeight - v.measuredHeight)
                viewModel.loadRedditTopList()
            }
        )

        floatingButton.setOnClickListener(View.OnClickListener {
            recyclerView.visibility = View.GONE
            viewModel.refreshRedditPostList()
        })
        /* viewModel.loadLocalRedditTopList() */
    }

}

class SomeViewModelFactory(
    private val repository: RedditPostRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = RedditPostViewModel(repository) as T
}


