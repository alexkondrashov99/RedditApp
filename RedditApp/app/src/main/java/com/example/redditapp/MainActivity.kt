package com.example.redditapp


import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.redditapp.api.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.File


const val fetchingDataSize = 10
const val maxDataSize = 50
val fetchingDataTime = REDDIT_T.HOUR

var isLoading = false;
class MainActivity : AppCompatActivity(),FeedNetworkDisplayer,FeedDatabaseDisplayer
{

    lateinit var progressBar: ProgressBar
    lateinit var recyclerView:RecyclerView
    lateinit var redditAdapter: RedditAdapter
    lateinit var nestedScrollView: NestedScrollView
    lateinit var floatingButton:FloatingActionButton
    var dataList = ArrayList<RedditData>()
    val dbHelper = DBHelper(this)
    var isRefresh:Boolean = false //if true - we need to REFRESH data, else - need only to UPDATE data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.GONE

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        nestedScrollView = findViewById(R.id.nestedScrollView)
        nestedScrollView.setOnScrollChangeListener( NestedScrollView.OnScrollChangeListener
        { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if(scrollY>= v.getChildAt(0).measuredHeight - v.measuredHeight)
                if(dataList.size < maxDataSize){
                    progressBar.visibility = View.VISIBLE
                    val after = getRedditAfterPeferences(this)
                    if(after != null)
                        if(!isLoading){
                            isRefresh = false
                            isLoading = true
                            fetchRedditData(limit = fetchingDataSize, t = fetchingDataTime, activity = this,after = after)
                        }
                }
        })

        floatingButton = findViewById(R.id.fab)
        floatingButton.setOnClickListener(View.OnClickListener {
            if(!isLoading){
                isRefresh = true
                isLoading = true
                recyclerView.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
                cleanImageDir(this)
                fetchRedditData(limit = fetchingDataSize, t = fetchingDataTime, activity = this)
            }

        })

        //Load data on application start
        if(dbHelper.getDataCount()>0){
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
            dbHelper.loadDataFromDatabase()
        }


    }

    override fun onNetworkLoadFinished(data: ArrayList<RedditData>) {
        if(isRefresh){
            dbHelper.refreshDataList(data)
        }
        else{
            dbHelper.updateDataList(data)
        }

    }
    override fun onDatabaseLoadFinished(data: ArrayList<RedditData>) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        dataList = data
        redditAdapter = RedditAdapter(dataList.toTypedArray())
        recyclerView.adapter = redditAdapter
        isLoading = false

    }

    override fun getParentContext(): Context {
        return this
    }
    override fun onNetworkError(error: String) {
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        Toast.makeText(this,"Отсутствует соединение с сетью...",Toast.LENGTH_LONG).show()
        isLoading = false

    }
    override fun onDatabaseError(error: String) {
        recyclerView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
        Toast.makeText(this,"Ошибка базы данных",Toast.LENGTH_LONG).show()
        isLoading = false
    }

    //Delete old images before fetching network data
    private fun cleanImageDir(context: Context):Boolean{
        val storageDir = File(
            context.applicationInfo.dataDir + imageDirectory
        )
        if (storageDir.exists()) {
            return storageDir.deleteRecursively()
        }
        else return false
    }

}