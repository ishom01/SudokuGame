package id.ishom.sudokuapp

import android.util.Log

fun HashMap<Int, ArrayList<Int?>>.toBoardDisplay(): ArrayList<Board> {
    val boards = ArrayList<Board>()
    for (positionX in 0 until this.keys.size) {
        val values = this[positionX]!!
        for (positionY in 0 until values.size) {
            val value = values[positionY]
            boards.add(Board(positionX, positionY, value, value != null, false))
        }
    }
    return boards
}

fun ArrayList<Board>.toMaps(): HashMap<Int, ArrayList<Int?>> {
    val maps = hashMapOf<Int, ArrayList<Int?>>()
    for (board in this) {
        if (board.positionX in maps) {
            maps[board.positionX]?.add(board.value)
        } else {
            maps[board.positionX] = arrayListOf(board.value)
        }
    }
    return maps
}

fun HashMap<Int, ArrayList<Int?>>.checkAnswer(): Boolean {
    for (position in 0 until this.keys.size) {
        if (!this.checkHorizontalValue(position) && !this.checkHorizontalValue(position)) {
            Log.e("Status Debug", "$position : false")
        } else {
            Log.e("Status Debug", "$position : true")
        }
    }
    for (xIndex in 0 until 3) {
        for (yIndex in 0 until 3) {
            if (!this.checkBoxValue(xIndex, yIndex)) {
                Log.e("Status Debug", "$xIndex, $yIndex : false")
            } else {
                Log.e("Status Debug", "$xIndex, $yIndex : true")
            }
        }
    }
    return true
}

fun HashMap<Int, ArrayList<Int?>>.checkBoxValue(xIndex: Int, yIndex: Int): Boolean {
    /*
        Box = [
            this[3xIndex][3yIndex], this[3xIndex][3yIndex + 1], this[3xIndex][3yIndex + 2]
            this[3xIndex + 1][3yIndex], this[3xIndex + 1][3yIndex + 1], this[3xIndex + 1][3yIndex + 2]
            this[3xIndex + 2][3yIndex], this[3xIndex + 2][3yIndex + 1], this[3xIndex + 2][3yIndex + 2]
        ]
        Using maps of each value is have duplicate counter or not
     */
    val temps = hashMapOf<Int, Int>()

    // hack its change [0,1,2 to -> 0], [3,4,5 to -> 1], [6,7,8 to -> 2]
    val roundedXIndex = xIndex / 3
    val roundedYIndex = yIndex / 3

    val boxMinXIndex = roundedXIndex * 3
    val boxMaxXIndex = (roundedXIndex * 3) + 2
    val boxMinYIndex = roundedYIndex * 3
    val boxMaxYIndex = (roundedYIndex * 3) + 2
    for (positionX in boxMinXIndex..boxMaxXIndex) {
        for (positionY in boxMinYIndex..boxMaxYIndex) {
            val value = this[positionX]!![positionY]?: continue
            if (value in temps) {
                val counter = temps[value]!!
                temps[value] = counter + 1
            } else {
                temps[value] = 1
            }
        }
    }
    // if value counter which have counter > 1 (its mean have duplicate keys)
    return temps.filterValues { it > 1 }.isEmpty()
}

fun HashMap<Int, ArrayList<Int?>>.checkVerticalValue(index: Int): Boolean {
    /*
        For checking for vertical value is valid or not,
        Using maps of each value is have duplicate counter or not
     */
    val temps = hashMapOf<Int, Int>()
    for (values in this.values) {
        val value = values[index]?: continue
        if (value in temps) {
            val counter = temps[value]!!
            temps[value] = counter + 1
        } else {
            temps[value] = 1
        }
    }
    // if value counter which have counter > 1 (its mean have duplicate keys)
    return temps.filterValues { it > 1 }.isEmpty()
}

fun HashMap<Int, ArrayList<Int?>>.checkHorizontalValue(index: Int): Boolean {
    /*
        For checking for horizontal value is valid or not,
        Using maps of each value is have duplicate counter or not
     */
    val temps = hashMapOf<Int, Int>()
    for (value in this[index]!!) {
        val value = value ?: continue
        if (value in temps) {
            val counter = temps[value]!!
            temps[value] = counter + 1
        } else {
            temps[value] = 1
        }
    }
    // if value counter which have counter > 1 (its mean have duplicate keys)
    return temps.filterValues { it > 1 }.isEmpty()
}

