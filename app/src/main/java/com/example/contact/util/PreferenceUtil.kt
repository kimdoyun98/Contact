package com.example.contact.util

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONArray

class PreferenceUtil (context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("prefs_name", Context.MODE_PRIVATE)

    fun getString(key: String, defValue: String): String {
        return prefs.getString(key, defValue).toString()
    }

    fun setString(key: String, str: String?) {
        prefs.edit().putString(key, str).apply()
    }

    private fun recentSearch(): String {
        return prefs.getString("recent_search", JSONArray().toString()).toString()
    }

    fun getRecentSearch(): JSONArray = JSONArray(recentSearch())


    fun setRecentSearch(str: String?) =
        prefs.edit().putString("recent_search", str).apply()


    /**
     * firebase token : fb_token
     * kakao token : kakao_token
     */
}