package com.example.redditapp

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.redditapp.api.BASE_URL
import com.example.redditapp.api.RedditData

private fun getTimeDifferenceString(created_utc:Long):String{
    val difference = System.currentTimeMillis() - created_utc*1000
    val resultSeconds:Long = difference/1000
    if(resultSeconds>=60){
        val resultMin:Long = resultSeconds/60
        if (resultMin>=60){
            val resultHours:Long = resultMin/60
            if (resultHours>=24){
                val resultDays:Long = resultHours/24
                return "$resultDays days ago"
            } else return "$resultHours hours ago"
        }else return "$resultMin mins ago"
    }else return "$resultSeconds sec ago"
}

class RedditAdapter(val redditData: Array<RedditData>) :
    RecyclerView.Adapter<RedditAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val twComments: TextView
        val twAuthor: TextView
        val twTitle: TextView
        val twSubreddit: TextView
        val twUpvotes: TextView
        val twTime: TextView
        val iwThumbnail:ImageView

        init {
            twComments = view.findViewById(R.id.TWcomments)
            twAuthor = view.findViewById(R.id.TWauthor)
            twTitle = view.findViewById(R.id.TWtitle)
            twSubreddit = view.findViewById(R.id.TWsubreddit)
            twUpvotes = view.findViewById(R.id.TWupvotes)
            twTime = view.findViewById(R.id.TWtime)
            iwThumbnail = view.findViewById(R.id.IWthumbnail)
        }
    }

    private fun openCustomTab(url: String?,context: Context) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(context, R.color.purple_200))
        builder.addDefaultShareMenuItem()
        val customTabsIntent: CustomTabsIntent = builder.build()
        customTabsIntent.launchUrl(context, Uri.parse(url))
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reddit_post, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.twUpvotes.text = if(redditData[position].upvotes > 1000) "${redditData[position].upvotes/1000}K" else redditData[position].upvotes.toString()
        viewHolder.twComments.text = redditData[position].numComments.toString()
        viewHolder.twAuthor.text = "Posted by u/" + redditData[position].author
        viewHolder.twTitle.text = redditData[position].title
        viewHolder.twSubreddit.text = redditData[position].subreddit
        if (redditData[position].thumbnail != null){
            viewHolder.iwThumbnail.setImageBitmap(redditData[position].thumbnail)
            viewHolder.iwThumbnail.visibility = View.VISIBLE
        }
        else viewHolder.iwThumbnail.visibility = View.GONE

        val dateString = getTimeDifferenceString(redditData[position].createdUTC)
        viewHolder.twTime.text = " $dateString"

        viewHolder.itemView.setOnClickListener(
            View.OnClickListener {
                openCustomTab(BASE_URL+redditData[position].url,viewHolder.itemView.context)
             })

    }
    override fun getItemCount() = redditData.size

}