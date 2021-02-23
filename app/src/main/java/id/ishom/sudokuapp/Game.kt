package id.ishom.sudokuapp

import android.content.Context
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class Game(context: Context) {
    var preferences: Preferences = Preferences(context)

    var isPlaying: Boolean
        set(value) = preferences.set("is-playing", value)
        get() = preferences.getBoolean("is-playing")

    var gameTime: Long
        set(value) = preferences.set("game-time", value)
        get() = preferences.getLong("game-time")

    fun saveBoard(boards: ArrayList<Board>) {
        val jsonArray = JSONArray()
        for (board in boards) {
            val jsonObject = JSONObject()
            jsonObject.put("positionX", board.positionX)
            jsonObject.put("positionY", board.positionY)
            jsonObject.put("value", board.value ?: 0) // change null to zero for saving
            jsonObject.put("isQuestion", board.isQuestion)
            jsonObject.put("isFalse", board.isFalse)
            jsonObject.put("isValid", board.isValid)
            jsonArray.put(jsonObject)
        }
        preferences["boards"] = jsonArray.toString()
    }

    fun loadPuzzle() : ArrayList<Board>? {
        if (preferences.sharedPreferences.contains("boards")) {
            val boardJsonString = preferences.getString("boards")
            val boardJsons = Gson().fromJson(boardJsonString, JSONArray::class.java)
            val boards = arrayListOf<Board>()
            for (index in 0 until boardJsons.length()) {
                val jsonObject = boardJsons.getJSONObject(index)

                var value = if (jsonObject.getInt("value") != 0)
                    jsonObject.getInt("value")
                else
                    null

                boards.add(Board(
                    jsonObject.getInt("positionX"),
                    jsonObject.getInt("positionY"),
                    value,
                    jsonObject.getBoolean("isQuestion"),
                    jsonObject.getBoolean("isFalse"),
                    jsonObject.getBoolean("isValid")
                ))
            }
            return boards
        } else {
            return null
        }
    }
}