package com.example.redditapp

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.RedditPost
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.DiffUtil
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class RedditPostActivity: AppCompatActivity() {

    /**
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

    /**
    ██╗░░░██╗██╗███████╗░██╗░░░░░░░██╗███╗░░░███╗░█████╗░██████╗░███████╗██╗░░░░░
    ██║░░░██║██║██╔════╝░██║░░██╗░░██║████╗░████║██╔══██╗██╔══██╗██╔════╝██║░░░░░
    ╚██╗░██╔╝██║█████╗░░░╚██╗████╗██╔╝██╔████╔██║██║░░██║██║░░██║█████╗░░██║░░░░░
    ░╚████╔╝░██║██╔══╝░░░░████╔═████║░██║╚██╔╝██║██║░░██║██║░░██║██╔══╝░░██║░░░░░
    ░░╚██╔╝░░██║███████╗░░╚██╔╝░╚██╔╝░██║░╚═╝░██║╚█████╔╝██████╔╝███████╗███████╗
    ░░░╚═╝░░░╚═╝╚══════╝░░░╚═╝░░░╚═╝░░╚═╝░░░░░╚═╝░╚════╝░╚═════╝░╚══════╝╚══════╝
     */
//    private val viewModel:RedditPostViewModel =
//        Injector
//            .provideSomeViewModelFactory()
//            .create(RedditPostViewModel::class.java)


    private val viewModel: RedditPostViewModel by viewModel() /** inject<RedditPostViewModel>() **/
    /*
    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░
    █████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗█████╗
    ╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝╚════╝
    ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░*/

    /**
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

    private var redditAdapter:RedditAdapter = RedditAdapter(mutableListOf())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.redditPostList.observe(this){ dataList ->
            if(recyclerView.adapter == null) {
                recyclerView.adapter = RedditAdapter(dataList.toMutableList())
            }
            else {
                val adapter = recyclerView.adapter as RedditAdapter
                val redditPostDiffUtilCallback = RedditPostDiffUtilCallback(adapter.redditData, dataList)
                val redditPostDiffResult = DiffUtil.calculateDiff(redditPostDiffUtilCallback)
                adapter.redditData = dataList.toMutableList()
                redditPostDiffResult.dispatchUpdatesTo(adapter)

            }
        }
        viewModel.errorMessage.observe(this){
            it?.let { text ->
                CustomRedditSnapbar.make(
                    layoutInflater,
                    findViewById(R.id.rootView),
                    text,
                    Snackbar.LENGTH_LONG)
                    .show()
            }
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
        recyclerView.adapter = redditAdapter
        nestedScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { view, _, scrollY, _, _ ->
                if(scrollY>= view.getChildAt(0).measuredHeight - view.measuredHeight)
                viewModel.loadRedditTopList()
            }
        )

        floatingButton.setOnClickListener {
            viewModel.refreshRedditPostList()
        }

        viewModel.init()
    }
}


