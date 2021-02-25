package id.ishom.sudokuapp

import android.os.Handler
import android.util.Log
import android.view.autofill.AutofillValue

/*
    Engine Game extensions created by Ishom
 */

fun HashMap<Int, ArrayList<Int?>>.toBoardDisplay(): ArrayList<Board> {
    /*
        This has made for change board maps to board list
     */
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
    /*
        This has made for change board list to baord maps
     */
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

fun HashMap<Int, ArrayList<Int?>>.checkBoxValue(xIndex: Int, yIndex: Int, dataValue: Int): Boolean {
    /*
        Box = [
            this[3xIndex][3yIndex], this[3xIndex][3yIndex + 1], this[3xIndex][3yIndex + 2]
            this[3xIndex + 1][3yIndex], this[3xIndex + 1][3yIndex + 1], this[3xIndex + 1][3yIndex + 2]
            this[3xIndex + 2][3yIndex], this[3xIndex + 2][3yIndex + 1], this[3xIndex + 2][3yIndex + 2]
        ]
        Using maps of each value is have duplicate counter or not
     */
    val temps = arrayListOf<Int>()
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
            temps.add(value)
        }
    }
    return dataValue !in temps
}

fun HashMap<Int, ArrayList<Int?>>.checkVerticalValue(index: Int, dataValue: Int): Boolean {
    /*
        For checking for vertical value is valid or not,
        Using maps of each value is have duplicate counter or not
     */
    val temps = arrayListOf<Int>()
    for (values in this.values) {
        val value = values[index]?: continue
        temps.add(value)
    }
    return dataValue !in temps
}

fun HashMap<Int, ArrayList<Int?>>.checkRowValue(xIndex: Int, yIndex: Int, value: Int): Boolean {
    return this.checkHorizontalValue(xIndex, value) && this.checkVerticalValue(yIndex, value) && checkBoxValue(xIndex, yIndex, value)
}

fun HashMap<Int, ArrayList<Int?>>.checkHorizontalValue(index: Int, dataValue: Int): Boolean {
    return dataValue !in this[index]!!
}

fun HashMap<Int, ArrayList<Int?>>.findEmptyBoard(): Pair<Int, Int> {
    for (xIndex in 0 until this.size) {
       val values =  this[xIndex]!!
        for (yIndex in 0 until this.size) {
            if (values[yIndex] == null) {
                return Pair(xIndex, yIndex)
            }
        }
    }
    return Pair(-1, -1)
}

fun HashMap<Int, ArrayList<Int?>>.checkSolution(): Boolean {
    val emptyCoordinateBoard = this.findEmptyBoard()
    if (emptyCoordinateBoard == Pair(-1, -1)) {
        return true
    }

    val xIndex = emptyCoordinateBoard.first
    val yIndex = emptyCoordinateBoard.second

    for (value in 1..9) {
        val isValid = this.checkRowValue(xIndex, yIndex, value)
        if (isValid) {
            this[xIndex]?.set(yIndex, value)
            if (this.checkSolution())
                return true
            this[xIndex]?.set(yIndex, null)
        }
    }
    return false
}

fun HashMap<Int, ArrayList<Int?>>.findSolution(): Boolean {
    val emptyCoordinateBoard = this.findEmptyBoard()
    if (emptyCoordinateBoard == Pair(-1, -1)) {
        return true
    }

    val xIndex = emptyCoordinateBoard.first
    val yIndex = emptyCoordinateBoard.second

    for (value in 1..9) {
        val isValid = checkRowValue(xIndex, yIndex, value)
        if (isValid) {
            this[xIndex]?.set(yIndex, value)
            if (findSolution())
                return true
            this[xIndex]?.set(yIndex, null)
        }
    }
    return false
}

fun Handler.start(runnable: Runnable) {
    this.postDelayed(runnable, 1000)
}

fun Handler.stop() {
    this.removeCallbacksAndMessages(null)
}

fun Handler.restart(runnable: Runnable) {
    this.stop()
    this.start(runnable)
}
