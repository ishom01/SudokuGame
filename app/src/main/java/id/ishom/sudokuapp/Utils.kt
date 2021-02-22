package id.ishom.sudokuapp

import android.util.Log

fun HashMap<Int, ArrayList<Int?>>.toDisplay(): ArrayList<Board> {
    val boards = ArrayList<Board>()
    for (positionX in 0 until this.keys.size) {
        val values = this[positionX]!!
        for (positionY in 0 until values.size) {
            boards.add(Board(positionX, positionY, values[positionY], false))
        }
    }
    return boards
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
    val boxMinXIndex = xIndex * 3
    val boxMaxXIndex = (xIndex * 3) + 2
    val boxMinYIndex = yIndex * 3
    val boxMaxYIndex = (yIndex * 3) + 2
    for (positionX in boxMinXIndex..boxMaxXIndex) {
        for (positionY in boxMinYIndex..boxMaxYIndex) {
            val value = this[positionX]!![positionY]?: break
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
    for (position in 0 until this.keys.size) {
        val value = this[position]!![index]?: break
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
        val value = value ?: break
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

