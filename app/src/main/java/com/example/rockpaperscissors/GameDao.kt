package com.example.rockpaperscissors

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDao {
    @Query("SELECT * FROM game_table")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(product: Game)

    @Delete
    suspend fun deleteGame(product: Game)

    @Query("DELETE FROM game_table")
    suspend fun deleteAllGame()
}