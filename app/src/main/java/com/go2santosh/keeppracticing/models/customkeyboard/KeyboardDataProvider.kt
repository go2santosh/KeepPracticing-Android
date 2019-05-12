package com.go2santosh.keeppracticing.models.customkeyboard

import android.util.Log
import com.go2santosh.keeppracticing.MainApplication
import org.json.JSONArray
import org.json.JSONException
import java.util.ArrayList

object KeyboardDataProvider {

    private const val logMessageSource = "KeyboardDataProvider"
    private const val dataFileName = "data/custom_keyboards.json"

    private var keyboards: ArrayList<KeyboardEntity> = ArrayList()

    init {
        loadKeyboards()
    }

    private fun loadKeyboards() {
        try {
            val jsonString = MainApplication
                .applicationContext()
                .assets
                .open(dataFileName)
                .bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(jsonString)
            for (i in 0 until jsonArray.length()) {
                val entity = KeyboardEntity(jsonArray.getJSONObject(i).toString())
                keyboards.add(entity)
            }
        } catch (jsonException: JSONException) {
            Log.w(logMessageSource, "Json string failed to deserialize.")
        }
    }

    fun keyboard(name: String): KeyboardEntity {
        return keyboards.first { it.name == name }
    }
}