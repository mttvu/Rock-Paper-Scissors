package com.example.rockpaperscissors

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.content_history.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
    private val gameHistory = arrayListOf<Game>()
    private lateinit var gameAdapter: GameAdapter
    private lateinit var gameRepository: GameRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        gameAdapter = GameAdapter(gameHistory, this)
        gameRepository = GameRepository(this)
        initViews()

    }

    private fun initViews() {
        rvGameHistory.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rvGameHistory.adapter = gameAdapter
        rvGameHistory.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        getGameHistoryFromDatabase()
    }

    private fun getGameHistoryFromDatabase() {
        mainScope.launch {
            val gameHistory = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }
            this@HistoryActivity.gameHistory.clear()
            this@HistoryActivity.gameHistory.addAll(gameHistory)
            this@HistoryActivity.gameAdapter.notifyDataSetChanged()
        }
    }

    private fun deleteGameHistory() {
        mainScope.launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAllGame()
            }
            this@HistoryActivity.gameHistory.clear()
            this@HistoryActivity.gameAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_history, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete -> {
                deleteGameHistory()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }


            else -> super.onOptionsItemSelected(item)
        }
    }
}
