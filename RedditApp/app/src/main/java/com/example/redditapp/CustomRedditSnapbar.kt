package com.example.redditapp

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

object CustomRedditSnapbar {
    fun make(inflater: LayoutInflater ,view: View, text: CharSequence, duration: Int ): Snackbar{
        val snackbar = Snackbar.make(view, "", duration)
        val customSnackView = inflater.inflate(R.layout.imagesnackbar, null);
        val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout?
        customSnackView.findViewById<TextView>(R.id.snackbar_text).text = text
        snackbarLayout?.setPadding(0, 0, 0, 0);
        snackbarLayout?.addView(customSnackView)
        return snackbar
    }
}