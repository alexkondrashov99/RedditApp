package com.example.redditapp

import org.junit.Test
import android.R.attr.password
import android.content.res.Resources

import com.example.data.R
import com.example.data.datasource.redditpostdatasourceimpl.*
import com.example.data.datasource.redditpostdatasourceinterface.RedditPostRemoteDataSource
import com.google.gson.JsonObject
import org.junit.Before
import java.lang.Exception
import com.google.gson.JsonParser
import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.BeforeEach

class RedditPostJsonParserTest {

    companion object{
        var jsonObject: JsonObject = JsonObject()

    }

    @Before
    open fun setUpJson() {
        jsonObject.add("data",JsonObject())
        jsonObject["data"].asJsonObject.addProperty("after","t3_88ll08")
        jsonObject["data"].asJsonObject.add("children",JsonArray())
        val jsonChildren = jsonObject["data"].asJsonObject["children"].asJsonArray

        val typicalDataObject = JsonObject()
        typicalDataObject.add("data",JsonObject())
        val typicalInnerData = typicalDataObject["data"].asJsonObject
        with(typicalInnerData){
            addProperty(json_title,"some test title")
            addProperty(json_author,"some test author")
            addProperty(json_created_utc,1592410647.0)
            addProperty(json_num_comments,19999)
            addProperty(json_url,"/r/pics/comments/haucpf/ive_found_a_few_funny_memories_during_lockdown/")

            addProperty(json_upvotes,25909)
            addProperty(json_subreddit,"r/wallstreetbets")
            addProperty(json_thumbnail,"https://b.thumbs.redditmedia.com/3bOsV7-uLwCGOo37i3CeSG4n3XBD7DI11sIdl3q97YY.jpg")
        }
        for(i in 1..10){
            jsonChildren.add(typicalDataObject)
        }
    }

    @Test
    fun `parsers gets 10 reddit posts return true`(){
        val result = jsonObject.toRedditPostList()
        assertThat(result.size == 10).isTrue()
    }
}