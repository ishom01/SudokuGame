package id.ishom.sudokuapp

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {

    var sharedPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    constructor(context: Context, key: String) : this(context) {
        sharedPreferences = context.getSharedPreferences(key, 0)
        editor = sharedPreferences.edit()
    }

    init {
        sharedPreferences = context.getSharedPreferences("preferences", 0)
        editor = sharedPreferences.edit()
    }

    operator fun set(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
    }

    operator fun set(key: String, boolean: Boolean) {
        editor.putBoolean(key, boolean)
        editor.commit()
    }

    operator fun set(key: String, value: Long) {
        editor.putLong(key, value)
        editor.commit()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun getLong(key: String): Long {
        return sharedPreferences.getLong(key, 0)
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}