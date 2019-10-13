package com.example.rockpaperscissors

import androidx.room.TypeConverter

class ResultConverter {
    @TypeConverter
    fun toOrdinal(result: Result): Int = result.ordinal

    @TypeConverter
    fun toTypeA(ordinal: Int): Result = Result.values().first { it.ordinal == ordinal }
}