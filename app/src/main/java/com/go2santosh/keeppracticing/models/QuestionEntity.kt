package com.go2santosh.keeppracticing.models

import org.json.JSONObject

class QuestionEntity(json: String) : JSONObject(json) {
    val question: String? = this.optString("question")
    val inputType: String? = this.optString("inputType")
    val inputCount: Int? = this.optInt("inputCount")
    val timeout: Int? = this.optInt("timeout")
    private val jsonArray = this.optJSONArray("answers")
    val answers = ArrayList<String>()

    init {
        val jsonArray = this.optJSONArray("answers")
        for (i in 0 until jsonArray.length()) {
            val entity = jsonArray[i].toString()
            answers.add(entity)
        }
    }
}