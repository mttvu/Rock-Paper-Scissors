package com.example.rockpaperscissors

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_rock_paper_scissors.*
import kotlinx.android.synthetic.main.content_rock_paper_scissors.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.*

class RockPaperScissorsActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private lateinit var computerMove: Move
    private lateinit var userMove: Move
    private lateinit var result: Result
    private val gameHistory = arrayListOf<Game>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rock_paper_scissors)
        setSupportActionBar(toolbar)
        gameRepository = GameRepository(this)
        initViews()
    }

    private fun initViews() {
        btnRock.setOnClickListener { onClick(btnRock) }
        btnPaper.setOnClickListener { onClick(btnPaper) }
        btnScissors.setOnClickListener { onClick(btnScissors) }
        getGameHistoryFromDatabase()
        setAllTimeStatistics()
    }

    override fun onClick(view: View) {
        //set userMove based on button clicked
        when (view.id) {
            R.id.btnRock -> userMove = Move.ROCK

            R.id.btnPaper -> userMove = Move.PAPER

            R.id.btnScissors -> userMove = Move.SCISSORS

        }
        setComputerMove()
        setResult(userMove, computerMove)
        addGame()
        updateView()
    }

    private fun startHistoryActivity() {
        val intent = Intent(this, HistoryActivity::class.java)
        startActivity(intent)
    }

    private fun setResult(uMove: Move, cMove: Move) {
        //set result based on user and computer move
        when (uMove) {
            Move.ROCK -> {
                result = when (cMove) {
                    Move.ROCK -> Result.DRAW

                    Move.PAPER -> Result.LOSE

                    Move.SCISSORS -> Result.WIN

                }

            }
            Move.PAPER -> {
                result = when (cMove) {
                    Move.ROCK -> Result.WIN

                    Move.PAPER -> Result.DRAW

                    Move.SCISSORS -> Result.LOSE

                }
            }
            Move.SCISSORS -> {
                result = when (cMove) {
                    Move.ROCK -> Result.LOSE

                    Move.PAPER -> Result.WIN

                    Move.SCISSORS -> Result.DRAW

                }
            }
        }
    }

    private fun setComputerMove() {
        //random move
        computerMove = Move.values().toList().shuffled().first()

    }

    private fun addGame() {
        mainScope.launch {
            val game = Game(
                date = Date(),
                computerMove = computerMove,
                userMove = userMove,
                result = result
            )

            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
            }
            getGameHistoryFromDatabase()

        }
    }

    private fun updateView() {
        //update view so the results of the game can be seen

        //included view
        var gameResult = findViewById<View>(R.id.gameResult)
        var tvResult = gameResult.findViewById<TextView>(R.id.tvResult)
        var ivUser = gameResult.findViewById<ImageView>(R.id.ivUser)
        var ivComputer = gameResult.findViewById<ImageView>(R.id.ivComputer)
        when (result) {
            Result.WIN -> tvResult.text = resources.getString(R.string.you_win)
            Result.LOSE -> tvResult.text = resources.getString(R.string.computer_win)
            Result.DRAW -> tvResult.text = resources.getString(R.string.draw)

        }
        ivUser.setImageDrawable(getImage(userMove))
        ivComputer.setImageDrawable(getImage(computerMove))
        getGameHistoryFromDatabase()
        setAllTimeStatistics()

    }

    private fun setAllTimeStatistics() {
        var win = 0
        var lose = 0
        var draw = 0
        //count the numbers of win, lose, draw
        gameHistory.forEach {
            when (it.result) {
                Result.WIN -> win++
                Result.LOSE -> lose++
                Result.DRAW -> draw++
            }
        }
        //update statistics textfield
        tvStatistics.text = getString(R.string.statistics, win, lose, draw)
    }

    private fun getGameHistoryFromDatabase() {
        mainScope.launch {
            val gameHistory = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }
            this@RockPaperScissorsActivity.gameHistory.clear()
            this@RockPaperScissorsActivity.gameHistory.addAll(gameHistory)
        }
    }

    private fun getImage(move: Move): Drawable {
        //get the corresponding image
        return when (move) {
            Move.ROCK -> resources.getDrawable(R.drawable.rock, null)

            Move.PAPER -> resources.getDrawable(R.drawable.paper, null)

            Move.SCISSORS -> resources.getDrawable(R.drawable.scissors, null)

        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_history -> {
                startHistoryActivity()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
