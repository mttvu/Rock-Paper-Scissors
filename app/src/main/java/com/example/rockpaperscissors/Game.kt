package com.example.rockpaperscissors

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.android.parcel.Parcelize
import java.util.*
@Parcelize
@Entity(tableName = "game_table")
data class Game(

    @ColumnInfo( name = "date")
    @TypeConverters(DateTypeConverter::class)
    var date: Date,

    @ColumnInfo( name = "computer_move")
    @TypeConverters(MoveConverter::class)
    var computerMove : Move,

    @ColumnInfo( name = "user_move")
    @TypeConverters(MoveConverter::class)
    var userMove : Move,

    @ColumnInfo( name = "result")
    @TypeConverters(ResultConverter::class)
    var result : Result,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo( name = "id")
    var id: Long? = null
) : Parcelable