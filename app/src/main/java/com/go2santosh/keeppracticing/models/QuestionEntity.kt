package com.go2santosh.keeppracticing.models

import org.json.JSONObject

class QuestionEntity(json: String) : JSONObject(json) {
    internal val question: String? = this.optString("question")
    internal val answers = ArrayList<String>()

    init {
        val jsonArray = this.optJSONArray("answers")
        for (i in 0 until jsonArray.length()) {
            val entity = jsonArray[i].toString()
            answers.add(entity)
        }
    }
}