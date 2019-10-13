package com.example.rockpaperscissors

import androidx.room.TypeConverter

class MoveConverter {
    @TypeConverter
    fun toOrdinal(move: Move): Int = move.ordinal

    @TypeConverter
    fun toMove(ordinal: Int): Move = Move.values().first { it.ordinal == ordinal }
}