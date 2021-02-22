package id.ishom.sudokuapp

data class Board(val positionX: Int, val positionY: Int, var value: Int?, var isFalse: Boolean = false) {
}