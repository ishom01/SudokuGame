package id.ishom.sudokuapp

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    var boardMaps = hashMapOf<Int, ArrayList<Int?>>()
    var boards = arrayListOf<Board>()
//    val gameAnswerMaps = hashMapOf<Int, ArrayList<Int?>>()

    lateinit var sudokuAdapter: SudokuAdapter
    lateinit var game: Game

    lateinit var runnable: Runnable
    var gameTime = 0L
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

        sudokuAdapter = SudokuAdapter(this, boards)
        recyclerView.apply {
            layoutManager = GridLayoutManager(this@MainActivity, 9)
            adapter = sudokuAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        // check if game data has loaded, try load to game data
        val loadBoards = game.loadBoards()
        if (loadBoards != null && game.isPlaying) {
            // setup load board
            boards = loadBoards
            boardMaps = loadBoards.toMaps()

            // load timer
            gameTime = game.gameTime
            setupTimerUI()
            isPause = true
            pauseImageView.setImageResource(R.drawable.ic_resume)
            timerLayout.show()
            solveButton.show()
        }
    }

    override fun onPause() {
        super.onPause()
        pauseGame()

        // save game progress
        game.gameTime = gameTime
        if (boards.isNotEmpty()) {
            game.saveBoards(boards)
        }
    }

    private fun showBoard() {
        sudokuAdapter.updateData(boards)
    }

    private fun hideBoard() {
        sudokuAdapter.selectedPosition = null
        sudokuAdapter.updateData(arrayListOf())
    }

    private fun setupTimerUI() {
        timeTextView.text = simpleDateFormat.format(gameTime * 1000)
    }

    fun onNewGameClicked(view: View) {
        if (game.isPlaying) {
            // if showing dialog pause the game
            pauseGame()

            val alertDialog = AlertDialog.Builder(this).create()
            alertDialog.setTitle("Are you sure want to restart this game?")
            alertDialog.setMessage("If you restart this game, your progress about the game will be removed.")
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES") { _, _ ->
                startGame()
            }
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO") { _, _ ->
                alertDialog.dismiss()
            }
            alertDialog.show()
        } else {
            startGame()
        }
    }

    fun startGame() {
        // refresh game cache
        game.clear()
        // set again
        game.isPlaying = true

        // updating time and time UI
        gameTime = 0
        setupTimerUI()
        isPause = false
        pauseImageView.setImageResource(R.drawable.ic_pause)
        timeHandler.restart(runnable)
        timerLayout.show()

        // Generate Game Puzzle
        generateGameData()
        sudokuAdapter.updateData(boards)
        solveButton.show()
    }

    fun pauseGame() {
        isPause = true
        pauseImageView.setImageResource(R.drawable.ic_resume)
        timeHandler.stop()

        // if this game is pause hide board
        hideBoard()
    }

    fun onPauseClicked(view: View) {
        if (isPause) {
            isPause = false
            pauseImageView.setImageResource(R.drawable.ic_pause)
            timeHandler.start(runnable)

            // if this game is resume show board
            showBoard()
        } else {
            pauseGame()
        }
    }

    fun onSolveClicked(view: View) {
        /*
            This method for showing sudoku results, check if player give up or not
         */
        pauseGame()
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("Would you like to see the answer to this sudoku?")
        alertDialog.setMessage("Seeing the answer from sudoku gets you, ending your game.")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES") { _, _ ->
            // Game Answer
            boardMaps[0] = arrayListOf(8, 4, 6, 9, 3, 7, 1, 5, 2)
            boardMaps[1] = arrayListOf(3, 1, 9, 6, 2, 5, 8, 4, 7)
            boardMaps[2] = arrayListOf(7, 5, 2, 1, 8, 4, 9, 6, 3)
            boardMaps[3] = arrayListOf(2, 8, 5, 7, 1, 3, 6, 9, 4)
            boardMaps[4] = arrayListOf(4, 6, 3, 8, 5, 9, 2, 7, 1)
            boardMaps[5] = arrayListOf(9, 7, 1, 2, 4, 6, 3, 8, 5)
            boardMaps[6] = arrayListOf(1, 2, 7, 5, 9, 8, 4, 3, 6)
            boardMaps[7] = arrayListOf(6, 3, 8, 4, 7, 1, 5, 2, 9)
            boardMaps[8] = arrayListOf(5, 9, 4, 3, 6, 2, 7, 1, 8)

            // displaying boards answer
            boards = boardMaps.toBoardDisplay()
            sudokuAdapter.updateData(boards)

            // if user want to show its mean give up the game
            game.clear()
            timerLayout.hide()
            solveButton.hide()
        }
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO") { _, _ ->
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun sudokuFinished() {
        pauseGame()
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.setTitle("YOU FINISHED THE SUDOKU!!")
        alertDialog.setMessage("Congratulation your finished sudoku with time ${simpleDateFormat.format(gameTime * 1000)}")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            // if user want to show its mean give up the game
            game.clear()
            timerLayout.hide()
            solveButton.hide()
        }
        alertDialog.show()
    }

    private fun generateGameData() {
        /*
        Notes: Set null value for empty answer
        */
//        Game Sample
        boardMaps[0] = arrayListOf(   8, null, null,    9,    3, null, null, null,    2)
        boardMaps[1] = arrayListOf(null, null,    9, null, null, null, null,    4, null)
        boardMaps[2] = arrayListOf(   7, null,    2,    1, null, null,    9,    6, null)
        boardMaps[3] = arrayListOf(   2, null, null, null, null, null, null,    9, null)
        boardMaps[4] = arrayListOf(null,    6, null, null, null, null, null,    7, null)
        boardMaps[5] = arrayListOf(null,    7, null, null, null,    6, null, null,    5)
        boardMaps[6] = arrayListOf(null,    2,    7, null, null,    8,    4, null,    6)
        boardMaps[7] = arrayListOf(null,    3, null, null, null, null,    5, null, null)
        boardMaps[8] = arrayListOf(   5, null, null, null,    6,    2, null, null,    8)

//        boardMaps[0] = arrayListOf(8, 4, 6, 9, 3, 7, 1, 5, 2)
//        boardMaps[1] = arrayListOf(3, 1, 9, 6, 2, 5, 8, 4, 7)
//        boardMaps[2] = arrayListOf(7, 5, 2, 1, 8, 4, 9, 6, 3)
//        boardMaps[3] = arrayListOf(2, 8, 5, 7, 1, 3, 6, 9, 4)
//        boardMaps[4] = arrayListOf(4, 6, 3, 8, 5, 9, 2, 7, 1)
//        boardMaps[5] = arrayListOf(9, 7, 1, 2, 4, 6, 3, 8, 5)
//        boardMaps[6] = arrayListOf(1, 2, 7, 5, 9, 8, 4, 3, 6)
//        boardMaps[7] = arrayListOf(6, 3, 8, 4, 7, 1, 5, 2, 9)
//        boardMaps[8] = arrayListOf(5, 9, 4, 3, 6, 2, 7, 1, null)

        boards = boardMaps.toBoardDisplay()
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

        // check if board.value is same with existing value, replace value with null
        if (value == board.value) {
            board.value = null
            boardMaps[board.positionX]!![board.positionY] = null
            board.isValid = true
        } else {
            board.value = value
            boardMaps[board.positionX]!![board.positionY] = value
            board.isValid = boardMaps.checkHorizontalValue(board.positionX, value) && boardMaps.checkVerticalValue(
                    board.positionY, value) && boardMaps.checkBoxValue(board.positionX, board.positionY, value)
        }

        boards[sudokuAdapter.selectedPosition!!] = board
        sudokuAdapter.updateData(boards)

        if (boards.none { !it.isValid || it.value == null }) {
            sudokuFinished()
        }
    }
}