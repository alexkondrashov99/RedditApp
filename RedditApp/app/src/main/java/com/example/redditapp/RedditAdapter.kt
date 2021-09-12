package com.example.redditapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.domain.models.RedditPost
import com.example.redditapp.api.BASE_URL
import androidx.recyclerview.widget.DiffUtil

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
private fun openCustomTab(url: String?,context: Context) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(context, R.color.purple_200))
    builder.addDefaultShareMenuItem()
    val customTabsIntent: CustomTabsIntent = builder.build()
    customTabsIntent.launchUrl(context, Uri.parse(url))
}

class RedditAdapter(var redditData: MutableList<RedditPost>) :
    RecyclerView.Adapter<RedditAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val twComments: TextView = view.findViewById(R.id.TWcomments)
        val twAuthor: TextView = view.findViewById(R.id.TWauthor)
        val twTitle: TextView = view.findViewById(R.id.TWtitle)
        val twSubreddit: TextView = view.findViewById(R.id.TWsubreddit)
        val twUpvotes: TextView = view.findViewById(R.id.TWupvotes)
        val twTime: TextView = view.findViewById(R.id.TWtime)
        val iwThumbnail:ImageView = view.findViewById(R.id.IWthumbnail)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.reddit_post, viewGroup, false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        viewHolder.twUpvotes.text =
            if(redditData[position].upvotes > 1000)
                "${redditData[position].upvotes/1000}K"
            else
                redditData[position].upvotes.toString()

        viewHolder.twComments.text = redditData[position].numComments.toString()
        viewHolder.twAuthor.text = "Posted by u/" + redditData[position].author
        viewHolder.twTitle.text = redditData[position].title
        viewHolder.twSubreddit.text = redditData[position].subreddit

        if (redditData[position].thumbnailURL != "null") {
            viewHolder.iwThumbnail.visibility = View.VISIBLE
            Glide.with(viewHolder.iwThumbnail)
                .asBitmap()
                .load(redditData[position].thumbnailURL)
                .into(object : CustomTarget<Bitmap>(){
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        viewHolder.iwThumbnail.setImageBitmap(resource)
                    }
                    override fun onLoadCleared(placeholder: Drawable?) {
                        // this is called when imageView is cleared on lifecycle call or for
                        // some other reason.
                        // if you are referencing the bitmap somewhere else too other than this imageView
                        // clear it here as you can no longer have the bitmap
                    }
                })
        }
        else
            viewHolder.iwThumbnail.visibility = View.GONE

        val dateString = getTimeDifferenceString(redditData[position].createdUTC)
        viewHolder.twTime.text = " $dateString"

        viewHolder.itemView.setOnClickListener {
            openCustomTab(BASE_URL + redditData[position].url, viewHolder.itemView.context)
        }

    }
    override fun getItemCount() = redditData.size
}

class RedditPostDiffUtilCallback(oldList: List<RedditPost>, newList: List<RedditPost>) :
    DiffUtil.Callback() {
    private val oldList: List<RedditPost> = oldList
    private val newList: List<RedditPost> = newList

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost: RedditPost = oldList[oldItemPosition]
        val newPost: RedditPost = newList[newItemPosition]
        return oldPost.url == newPost.url
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldPost: RedditPost = oldList[oldItemPosition]
        val newPost: RedditPost = newList[newItemPosition]
        return oldPost == newPost
    }

}