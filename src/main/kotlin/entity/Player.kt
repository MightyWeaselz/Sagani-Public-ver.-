package entity

import com.andreapivetta.kolor.Kolor
import edu.udo.cs.sopra.ntf.Color
import kotlinx.serialization.Serializable

/**
 * A [Player] is a player of the game.
 * @property name the name of the [Player]
 * @property color the color of the [Player]
 * @property playerType the [PlayerType] of the [Player]
 * @property currentDiscs the current number of disks of the [Player]
 * @property totalDiscs the total number of disks of the [Player]
 * @property score the current score of the [Player]
 */
@Serializable
data class Player(
    val name: String,
    val color: Color,
    val playerType: PlayerType,
    var lastScoreChangeMoveID: Int = 0
) {
    var score = 0
    var currentDiscs = 24
    var totalDiscs = 24

    /**
     * The grid owned by the player.
     */
    val grid: Grid = Grid()

    /**
     * @return converted [Color] to [com.andreapivetta.kolor.Color]
     */
    private fun getKolor(color: Color) = when (color) {
        Color.GREY -> com.andreapivetta.kolor.Color.LIGHT_GRAY
        Color.WHITE -> com.andreapivetta.kolor.Color.WHITE
        Color.BROWN -> com.andreapivetta.kolor.Color.LIGHT_RED
        Color.BLACK -> com.andreapivetta.kolor.Color.BLACK
    }

    /**
     * @return The string representation of the player.
     */
    override fun toString(): String {
        val formatString =
            "$name $currentDiscs/$totalDiscs $score${
                if (playerType != PlayerType.LOCAL_PLAYER) " ${playerType.name}" else ""
            }"

        val foreground =
            if (color == Color.BLACK) com.andreapivetta.kolor.Color.WHITE else com.andreapivetta.kolor.Color.BLACK

        return Kolor.background(Kolor.foreground(formatString, foreground), getKolor(color))
    }
}