package id.ishom.sudokuapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val gameMaps = hashMapOf<Int, ArrayList<Int?>>()
    var boards = arrayListOf<Board>()
//    val gameAnswerMaps = hashMapOf<Int, ArrayList<Int?>>()

    lateinit var sudokuAdapter: SudokuAdapter
    lateinit var game: Game

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        game = Game(this)

//        boards = if (game.loadPuzzle() != null) {
//            game.loadPuzzle()!!
//        } else {
//            generateGameData()
//            gameMaps.toBoardDisplay()
//        }
        generateGameData()
        gameMaps.toBoardDisplay().toMaps()
        boards = gameMaps.toBoardDisplay()

        sudokuAdapter = SudokuAdapter(this, boards)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 9)
            adapter = sudokuAdapter
        }
    }

    private fun setupGameData() {

    }

    private fun generateGameData() {
        /*
        Notes: Set null value for empty answer
        */
//        Game Sample
//        gameMaps[0] = arrayListOf(   8, null, null,    9,    3, null, null, null,    2)
//        gameMaps[1] = arrayListOf(null, null,    9, null, null, null, null,    4, null)
//        gameMaps[2] = arrayListOf(   7, null,    2,    1, null, null,    9,    6, null)
//        gameMaps[3] = arrayListOf(   2, null, null, null, null, null, null,    9, null)
//        gameMaps[4] = arrayListOf(null,    6, null, null, null, null, null,    7, null)
//        gameMaps[5] = arrayListOf(null,    7, null, null, null,    6, null, null,    5)
//        gameMaps[6] = arrayListOf(null,    2,    7, null, null,    8,    4, null,    6)
//        gameMaps[7] = arrayListOf(null,    3, null, null, null, null,    5, null, null)
//        gameMaps[8] = arrayListOf(   5, null, null, null,    6,    2, null, null,    8)

//        Game Result
//        gameMaps[0] = arrayListOf(   8,    4,    6,    9,    3,    7,    1,    5,    2)
//        gameMaps[1] = arrayListOf(   3,    1,    9,    6,    2,    5,    8,    4,    7)
//        gameMaps[2] = arrayListOf(   7,    5,    2,    1,    8,    4,    9,    6,    3)
//        gameMaps[3] = arrayListOf(   2,    8,    5,    7,    1,    3,    6,    9,    4)
//        gameMaps[4] = arrayListOf(   4,    6,    3,    8,    5,    9,    2,    7,    1)
//        gameMaps[5] = arrayListOf(   9,    7,    1,    2,    4,    6,    3,    8,    5)
//        gameMaps[6] = arrayListOf(   1,    2,    7,    5,    9,    8,    4,    3,    6)
//        gameMaps[7] = arrayListOf(   6,    3,    8,    4,    7,    1,    5,    2,    9)
//        gameMaps[8] = arrayListOf(   5,    9,    4,    3,    6,    2,    7,    1,    8)

        gameMaps[0] = arrayListOf(   8,    4,    6,    9,    3,    7,    1,    5,    2)
        gameMaps[1] = arrayListOf(   3,    1,    9,    6,    2,    5,    8,    4,    7)
        gameMaps[2] = arrayListOf(   7,    5,    2,    1,    8,    4,    9,    6,    3)
        gameMaps[3] = arrayListOf(   2,    8,    5,    7,    1,    3,    6,    9,    4)
        gameMaps[4] = arrayListOf(   4,    6,    3,    8,    5,    9,    2,    7,    1)
        gameMaps[5] = arrayListOf(   9,    7,    1,    2,    4,    6,    3,    8,    5)
        gameMaps[6] = arrayListOf(   1,    2,    7,    5,    9,    8,    4,    3,    6)
        gameMaps[7] = arrayListOf(   6,    3,    8,    4,    7,    1,    5,    2,    9)
        gameMaps[8] = arrayListOf(   5,    9,    4,    3,    6,    2,    7,    1, null)
    }

    fun onStartClicked(view: View) {

    }

    fun onSolveClicked(view: View) {

    }

    fun oneButtonClicked(view: View) {
        updateBoardValue(1)
    }

    fun twoButtonClicked(view: View) {
        updateBoardValue(2)
    }

    fun threeButtonClicked(view: View) {
        updateBoardValue(3)
    }

    fun fourButtonClicked(view: View) {
        updateBoardValue(4)
    }

    fun fiveButtonClicked(view: View) {
        updateBoardValue(5)
    }

    fun sixButtonClicked(view: View) {
        updateBoardValue(6)
    }

    fun sevenButtonClicked(view: View) {
        updateBoardValue(7)
    }

    fun eightButtonClicked(view: View) {
        updateBoardValue(8)
    }

    fun nineButtonClicked(view: View) {
        updateBoardValue(9)
    }

    private fun updateBoardValue(value: Int) {
        if (sudokuAdapter.selectedPosition == null) {
            return
        }

        val board = boards[sudokuAdapter.selectedPosition!!]
        board.value = value

        gameMaps[board.positionX]!![board.positionY] = value
        board.isValid = gameMaps.checkHorizontalValue(board.positionX) && gameMaps.checkVerticalValue(board.positionY) && gameMaps.checkBoxValue(board.positionX, board.positionY)

        boards[sudokuAdapter.selectedPosition!!] = board
        sudokuAdapter.updateData(boards)

        if (boards.none { !it.isValid || it.value == null }) {
            sudokuSolved()
        }
    }

    private fun sudokuSolved() {
        Toast.makeText(this, "YOU WIN!", Toast.LENGTH_LONG).show()
    }
}