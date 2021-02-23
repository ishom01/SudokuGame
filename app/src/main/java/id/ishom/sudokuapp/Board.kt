package id.ishom.sudokuapp

data class Board(
        val positionX: Int,
        val positionY: Int,
        var value: Int?,
        val isQuestion: Boolean = false,
        var isFalse: Boolean = false,
        var isValid: Boolean = true
)