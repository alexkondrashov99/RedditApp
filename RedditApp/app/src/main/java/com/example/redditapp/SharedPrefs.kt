package com.example.redditapp

import android.content.Context
import android.content.SharedPreferences



//To storage reddit 'after' data
const val sharedPreferences = "REDDIT_SHARED_PREFERENCES"
const val afterSharedPrefs = "REDDIT_AFTER"

fun setRedditAfterPreferences(context: Context, after:String) {
    val settings = context.getSharedPreferences(sharedPreferences,0)
    val editor = settings.edit()
    editor.putString(afterSharedPrefs, after)
    editor.apply()
}
fun getRedditAfterPeferences(context: Context):String? {
    val settings: SharedPreferences = context.getSharedPreferences(sharedPreferences, 0)
    return settings.getString(afterSharedPrefs,"")
}
fun clearRedditAfterPreferences(context: Context) {
    val settings = context.getSharedPreferences(sharedPreferences, 0)
    settings.edit().remove(afterSharedPrefs).commit()
}