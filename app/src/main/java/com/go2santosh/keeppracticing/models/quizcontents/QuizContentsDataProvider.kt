package com.go2santosh.keeppracticing.models.quizcontents

import android.util.Log
import com.go2santosh.keeppracticing.MainApplication
import org.json.JSONArray
import org.json.JSONException

object QuizContentsDataProvider {

    private const val logMessageSource = "QuizContentsData"
    private const val dataFileName = "data/quiz_contents.json"

    var topics: ArrayList<TopicEntity> = ArrayList()

    init {
        loadTopics()
    }

    private fun loadTopics() {
        try {
            val jsonString = MainApplication
                .applicationContext()
                .assets
                .open(dataFileName)
                .bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val entity = TopicEntity(jsonArray.getJSONObject(i).toString())
                topics.add(entity)
            }
        } catch (jsonException: JSONException) {
            Log.w(logMessageSource, "Json string failed to deserialize.")
        }
    }
}