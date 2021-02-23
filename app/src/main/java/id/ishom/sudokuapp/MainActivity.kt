package id.ishom.sudokuapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var boardMaps = hashMapOf<Int, ArrayList<Int?>>()
    var boards = arrayListOf<Board>()
//    val gameAnswerMaps = hashMapOf<Int, ArrayList<Int?>>()

    lateinit var sudokuAdapter: SudokuAdapter
    lateinit var game: Game

    lateinit var runnable: Runnable
    var gameTime = 1L
    var isPause = false
    val timeHandler = Handler()
    var simpleDateFormat = SimpleDateFormat("HH:mm:ss", Locale.UK)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // init classes to generate game and time
        game = Game(this)
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        runnable = Runnable {
            gameTime += 1
            setupTimerUI()
            timeHandler.postDelayed(runnable, 1000)
        }

        // check if game data has loaded, try load to game data
        val loadBoards = game.loadBoards()
        if (loadBoards != null) {
            boards = loadBoards
            boardMaps = loadBoards.toMaps()
            gameTime = game.gameTime
            timeHandler.start(runnable)
            timerLayout.show()
        }

        sudokuAdapter = SudokuAdapter(this, boards)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 9)
            adapter = sudokuAdapter
        }

//        generateGameData()
//        boardMaps.toBoardDisplay().toMaps()
//        boards = boardMaps.toBoardDisplay()
    }

    override fun onPause() {
        super.onPause()
        // If the activity is pause or closed, save the game progress
        game.gameTime = gameTime
        if (boards.isNotEmpty()) {
            game.saveBoards(boards)
        }
    }

    private fun setupTimerUI() {
        timeTextView.text = simpleDateFormat.format(gameTime * 1000)
    }

    fun onNewGameClicked(view: View) {
        // updating time and time UI
        gameTime = 0
        game.isPlaying = true
        isPause = false
        pauseImageView.setImageResource(R.drawable.ic_pause)
        timeHandler.restart(runnable)
        timerLayout.show()

        // Generate Game Puzzle
    }

    fun onPauseClicked(view: View) {
        if (isPause) {
            isPause = false
            pauseImageView.setImageResource(R.drawable.ic_pause)
            timeHandler.start(runnable)
        } else {
            isPause = true
            pauseImageView.setImageResource(R.drawable.ic_resume)
            timeHandler.stop()
        }
    }

    fun onSolveClicked(view: View) {

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

        boardMaps[0] = arrayListOf(   8,    4,    6,    9,    3,    7,    1,    5,    2)
        boardMaps[1] = arrayListOf(   3,    1,    9,    6,    2,    5,    8,    4,    7)
        boardMaps[2] = arrayListOf(   7,    5,    2,    1,    8,    4,    9,    6,    3)
        boardMaps[3] = arrayListOf(   2,    8,    5,    7,    1,    3,    6,    9,    4)
        boardMaps[4] = arrayListOf(   4,    6,    3,    8,    5,    9,    2,    7,    1)
        boardMaps[5] = arrayListOf(   9,    7,    1,    2,    4,    6,    3,    8,    5)
        boardMaps[6] = arrayListOf(   1,    2,    7,    5,    9,    8,    4,    3,    6)
        boardMaps[7] = arrayListOf(   6,    3,    8,    4,    7,    1,    5,    2,    9)
        boardMaps[8] = arrayListOf(   5,    9,    4,    3,    6,    2,    7,    1, null)
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

        boardMaps[board.positionX]!![board.positionY] = value
        board.isValid = boardMaps.checkHorizontalValue(board.positionX) && boardMaps.checkVerticalValue(board.positionY) && boardMaps.checkBoxValue(board.positionX, board.positionY)

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