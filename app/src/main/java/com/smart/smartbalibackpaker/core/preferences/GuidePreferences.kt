package com.smart.smartbalibackpaker.core.preferences

import android.content.Context
import android.content.SharedPreferences

class GuidePreferences(val context: Context) {
    companion object{
        const val GUIDE_KEY = "guide_key"
    }

    private var sharedPreferences = context.getSharedPreferences(GUIDE_KEY, 0)

    fun setState(key: String, value: String){
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getState(key: String): String? {
        return sharedPreferences.getString(key, "")
    }
}