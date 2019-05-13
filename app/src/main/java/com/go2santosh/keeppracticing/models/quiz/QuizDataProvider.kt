package com.go2santosh.keeppracticing.models.quiz

import android.util.Log
import com.go2santosh.keeppracticing.MainApplication
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class QuizDataProvider(val dataFileName: String = "data/sample_questions.json") {

    private val logMessageSource = "QuizDataProvider"

    var questions: ArrayList<QuestionEntity> = ArrayList()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        try {
            val jsonString = MainApplication
                .applicationContext()
                .assets
                .open(dataFileName)
                .bufferedReader().use { it.readText() }
            val jsonArray = JSONObject(jsonString).optJSONArray("questions")
            for (i in 0 until jsonArray.length()) {
                val entity = QuestionEntity(jsonArray.getJSONObject(i).toString())
                questions.add(entity)
            }
        } catch (jsonException: JSONException) {
            Log.w(logMessageSource, "Json string failed to deserialize.")
        }
    }
}