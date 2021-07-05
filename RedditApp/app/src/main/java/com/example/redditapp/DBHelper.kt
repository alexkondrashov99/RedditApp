package com.example.redditapp

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.redditapp.api.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream


const val TABLE_REDDIT_DATA = "table_reddit_data"
//*********************************************//
const val DATA_ID = "id"
const val DATA_TITLE = "title"
const val DATA_AUTHOR  = "author"
const val DATA_CREATED_UTC = "created_utc"
const val DATA_NUM_COMMENTS = "num_comments"
const val DATA_SUBREDDIT = "subreddit"
const val DATA_URL = "url"
const val DATA_UPVOTES = "ups"
const val DATA_THUMBNAIL_URL = "thumbnail_url"
const val DATA_THUMBNAIL_WIDTH = "thumbnail_width"
const val DATA_THUMBNAIL_HEIGHT = "thumbnail_height"
const val DATA_THUMBNAIL_PATH = "thumbnail_path"
//---------------------------------------------//
const val CREATE_TABLE_DATA =
    "CREATE TABLE "+TABLE_REDDIT_DATA+" ( "+
            DATA_ID+" integer primary key autoincrement," +
            DATA_TITLE+" Text," +
            DATA_AUTHOR+" Text," +
            DATA_SUBREDDIT+" Text," +
            DATA_CREATED_UTC+" Text," +
            DATA_URL+" Text," +
            DATA_UPVOTES+" Integer," +
            DATA_NUM_COMMENTS+" Integer," +
            DATA_THUMBNAIL_URL+" Text," +
            DATA_THUMBNAIL_WIDTH+" Integer," +
            DATA_THUMBNAIL_HEIGHT +" Integer," +
            DATA_THUMBNAIL_PATH +" TEXT )"
const val CLEAR_TABLE_DATA =  "DELETE FROM " + TABLE_REDDIT_DATA
const val LOG_TAG = "DATABASE"


interface FeedDatabaseDisplayer:FeedNetworkDisplayer {
    fun onDatabaseError(error:String)
    fun onDatabaseLoadFinished(data:ArrayList<RedditData>)
}

class DBHelper(activity: FeedDatabaseDisplayer) : SQLiteOpenHelper(activity.getParentContext(), DATABASE_NAME, null, DATABASE_VERSION) {

    val feedDatabaseDisplayer:FeedDatabaseDisplayer = activity
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_DATA)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "RedditTop50.db"
    }

    private fun clearTableData(){
        val db = writableDatabase
        db.execSQL(CLEAR_TABLE_DATA)
        db.close()
    }
    private fun saveToTableData(dataList:ArrayList<RedditData>){
        val db = writableDatabase
        for (data in dataList){
            val values = ContentValues()
            values.put(DATA_TITLE, data.title)
            values.put(DATA_AUTHOR, data.author)
            values.put(DATA_SUBREDDIT, data.subreddit)
            values.put(DATA_CREATED_UTC, data.createdUTC.toString())
            values.put(DATA_URL, data.url)
            values.put(DATA_UPVOTES, data.upvotes)
            values.put(DATA_NUM_COMMENTS,data.numComments);
            values.put(DATA_THUMBNAIL_URL, data.thumbnailURL)
            values.put(DATA_THUMBNAIL_PATH,data.thumbnailPath);
            db.insert(TABLE_REDDIT_DATA, null, values);
        }
        db.close()

        read_table(TABLE_REDDIT_DATA)
    }
    private fun getTableData():ArrayList<RedditData>{
        val dataList = ArrayList<RedditData>()
        val db = writableDatabase
        val c:Cursor

        c = db.rawQuery("SELECT * FROM "+ TABLE_REDDIT_DATA,null)
        if(c.moveToFirst())
            do
            {
                val title = c.getString(c.getColumnIndex(DATA_TITLE))
                val author = c.getString(c.getColumnIndex(DATA_AUTHOR))
                val subreddit = c.getString(c.getColumnIndex(DATA_SUBREDDIT))
                val createdUTC = c.getString(c.getColumnIndex(DATA_CREATED_UTC)).toDouble().toLong()
                val numComments = c.getInt(c.getColumnIndex(DATA_NUM_COMMENTS))
                val url = c.getString(c.getColumnIndex(DATA_URL))
                val upvotes = c.getInt(c.getColumnIndex(DATA_UPVOTES))
                val thumbnailURL = c.getString(c.getColumnIndex(DATA_THUMBNAIL_URL))
                val thumbnailWidth:Int = c.getInt(c.getColumnIndex(DATA_THUMBNAIL_WIDTH))
                val thumbnailHeight:Int = c.getInt(c.getColumnIndex(DATA_THUMBNAIL_HEIGHT))
                val thumbnailPath:String = c.getString(c.getColumnIndex(DATA_THUMBNAIL_PATH))

                val thumbnailBitmap = if(!thumbnailPath.isEmpty()){
                    getBitmap(thumbnailPath,thumbnailWidth,thumbnailHeight)
                }else{
                    null
                }

                val rData = RedditData(
                    title = title,
                    author = author,
                    createdUTC = createdUTC,
                    numComments = numComments,
                    url = url,
                    upvotes = upvotes,
                    subreddit = subreddit,
                    thumbnailURL = thumbnailURL,
                    thumbnailPath = thumbnailPath,
                    thumbnail = thumbnailBitmap
                )
                dataList.add(rData)
            } while(c.moveToNext())
        return dataList
    }

    //Just to get size of TABLE_DATA
    fun getDataCount():Int{
        val dataList = ArrayList<RedditData>()
        val db = writableDatabase
        val c:Cursor

        c = db.rawQuery("SELECT * FROM "+ TABLE_REDDIT_DATA,null)
        return c.count
    }

    //Just to load local data
    fun loadDataFromDatabase(){
        GlobalScope.launch(Dispatchers.IO){
            var dataList:ArrayList<RedditData>
            try{
                dataList = getTableData()
                withContext(Dispatchers.Main){
                    feedDatabaseDisplayer.onDatabaseLoadFinished(dataList)
                }

            }
            catch (e: Exception){
                Log.e("Main","Error: ${e.message}")
                withContext(Dispatchers.Main){
                    feedDatabaseDisplayer.onDatabaseError(e.message.toString())
                }
            }
        }
    }

    //To save new data WITH deleting old data
    fun refreshDataList(dataList:ArrayList<RedditData>){
        GlobalScope.launch(Dispatchers.IO){

            try{
                if(getDataCount() != 0)
                    clearTableData()
                saveToTableData(dataList)
                withContext(Dispatchers.Main){
                    feedDatabaseDisplayer.onDatabaseLoadFinished(dataList)
                }

            }
            catch (e: Exception){
                Log.e("Main","Error: ${e.message}")
                withContext(Dispatchers.Main){
                    feedDatabaseDisplayer.onDatabaseError(e.message.toString())
                }
            }
        }
    }

    //To save new data WITHOUT deleting old data
    fun updateDataList(dataList:ArrayList<RedditData>){
        GlobalScope.launch(Dispatchers.IO){
            try{
                saveToTableData(dataList)
                val updatedDataList = getTableData()
                withContext(Dispatchers.Main){
                    feedDatabaseDisplayer.onDatabaseLoadFinished(updatedDataList)
                }

            }
            catch (e: Exception){
                Log.e("Main","Error: ${e.message}")
                withContext(Dispatchers.Main){
                    feedDatabaseDisplayer.onDatabaseError(e.message.toString())
                }
            }
        }
    }

    //To display table to LOG
    fun read_table(tableName: String) {
        val db = this.writableDatabase
        val cursor: Cursor
        val selectQuery = "SELECT  * FROM $tableName"
        cursor = db.rawQuery(selectQuery, null)
        val str = cursor.columnNames
        logCursor(cursor, "TABLE_$tableName")
        db.close()
    }

    private fun getBitmap(path: String?,width:Int,height:Int): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val f = File(path)
            val options = BitmapFactory.Options()
            options.outHeight = height
            options.outWidth = width
            options.inPreferredConfig = Bitmap.Config.ARGB_8888
            bitmap = BitmapFactory.decodeStream(FileInputStream(f), null, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return bitmap
    }
    private fun logCursor(c: Cursor?, title: String) {
        if (c != null) {
            if (c.moveToFirst()) {
                Log.d(LOG_TAG, title + ". " + c.count + " rows")
                val sb = StringBuilder()
                do {
                    sb.setLength(0)
                    for (cn: String in c.columnNames) {
                        sb.append(
                            cn + " = "
                                    + c.getString(c.getColumnIndex(cn)) + "; "
                        )
                    }
                    Log.d(LOG_TAG, sb.toString())
                } while (c.moveToNext())
            }
        } else Log.d(LOG_TAG, "$title. Cursor is null")
    }

}