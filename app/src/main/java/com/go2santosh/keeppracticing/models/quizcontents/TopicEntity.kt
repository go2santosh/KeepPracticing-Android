package com.go2santosh.keeppracticing.models.quizcontents

import org.json.JSONObject

class TopicEntity(json: String) : JSONObject(json) {
    internal val subject: String? = this.optString("subject")
    internal val grade: String? = this.optString("grade")
    internal val domain: String? = this.optString("domain")
    internal val topic: String? = this.optString("topic")
    internal val quizFileName: String? = this.optString("quizFileName")
}