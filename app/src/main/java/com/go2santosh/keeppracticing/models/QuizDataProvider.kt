package com.go2santosh.keeppracticing.models

import android.util.Log
import com.go2santosh.keeppracticing.MainApplication
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

object QuizDataProvider {

    private const val logMessageSource = "QuizDataProvider"
    private const val dataFileName = "data/sample_questions.json"
    private const val questionsElementName = "questions"

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
            val jsonArray = JSONObject(jsonString).optJSONArray(questionsElementName)
            for (i in 0 until jsonArray.length()) {
                val entity = QuestionEntity(jsonArray.getJSONObject(i).toString())
                questions.add(entity)
            }
        } catch (jsonException: JSONException) {
            Log.w(logMessageSource, "Json string failed to deserialize.")
        }
    }
}