package com.go2santosh.keeppracticing.models.keyboardmodel

import org.json.JSONObject

class KeyboardEntity(json: String) : JSONObject(json) {
    internal val name: String? = this.optString("name")
    internal val configuration: KeyboardConfigurationEntity? = KeyboardConfigurationEntity(this.optJSONObject("configuration").toString())
}