package com.go2santosh.keeppracticing.models

import android.util.Log
import com.go2santosh.keeppracticing.MainApplication
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

object QuizDataProvider {

    val TAG = "QuizDataProvider"
    val DATA_FILE_NAME = "data/sample_questions.json"
    val QUESTIONS_ARRAY_NAME = "questions"

    var questions: ArrayList<QuestionEntity> = ArrayList<QuestionEntity>()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        try {
            val jsonString = MainApplication
                .applicationContext()
                .getAssets()
                .open(DATA_FILE_NAME)
                .bufferedReader().use { it.readText() }
            val jsonArray = JSONObject(jsonString).optJSONArray(QUESTIONS_ARRAY_NAME)
            for (i in 0 until jsonArray.length()) {
                val entity = QuestionEntity(jsonArray.getJSONObject(i).toString())
                questions.add(entity)
            }
        } catch (jsonException: JSONException) {
            Log.w(TAG, "Json string failed to deserialize.")
        }
    }
}