package com.example.rockpaperscissors

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.security.AccessController.getContext

class GameAdapter(private val games: List<Game>, val context: Context) :
    RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    /**
     * Creates and returns a ViewHolder object, inflating a standard layout called simple_list_item_1.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.game_result,
                parent,
                false
            )
        )
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return games.size
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvResult: TextView = itemView.findViewById(R.id.tvResult)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val ivUser: ImageView = itemView.findViewById(R.id.ivUser)
        private val ivComputer: ImageView = itemView.findViewById(R.id.ivComputer)


        fun bind(game: Game) {
            when (game.result) {
                Result.WIN -> tvResult.text = context.resources.getString(R.string.you_win)
                Result.LOSE -> tvResult.text = context.resources.getString(R.string.computer_win)
                Result.DRAW -> tvResult.text = context.resources.getString(R.string.draw)

            }

            tvDate.text = game.date.toString()
            ivUser.setImageDrawable(getImage(game.userMove))
            ivComputer.setImageDrawable(getImage(game.computerMove))

        }

        private fun getImage(move : Move): Drawable {
            return when (move) {
                Move.ROCK -> context.resources.getDrawable(R.drawable.rock,null)

                Move.PAPER -> context.resources.getDrawable(R.drawable.paper,null)

                Move.SCISSORS -> context.resources.getDrawable(R.drawable.scissors,null)

            }
        }
    }
}