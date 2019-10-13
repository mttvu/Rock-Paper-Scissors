package com.example.rockpaperscissors

import android.content.Context

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }

    suspend fun getAllGames(): List<Game> = gameDao.getAllGames()

    suspend fun insertGame(product: Game) = gameDao.insertGame(product)

    suspend fun deleteGame(product: Game) = gameDao.deleteGame(product)

    suspend fun deleteAllGame() = gameDao.deleteAllGame()

}