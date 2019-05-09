package com.go2santosh.keeppracticing.models.keyboardmodel

import org.json.JSONObject

class KeyboardConfigurationEntity(json: String) : JSONObject(json) {
    internal val firstLineKeys: String? = this.optString("firstLineKeys")
    internal val secondLineKeys: String? = this.optString("secondLineKeys")
}