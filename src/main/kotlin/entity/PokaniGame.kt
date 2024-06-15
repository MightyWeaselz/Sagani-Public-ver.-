package entity

import entity.components.BaseTile
import entity.move.AbstractMove
import kotlinx.serialization.Serializable

/**
 * A [PokaniGame] is a game of Pokani.
 * @property players the [List] of [Player]s of the game
 */
@Serializable
data class PokaniGame(
    val players: List<Player>
) {
    val drawPile = mutableListOf<BaseTile>()
    val offerDisplay = mutableListOf<BaseTile>()
    val intermezzoDisplay = mutableListOf<BaseTile>()
    val discardPile = mutableListOf<BaseTile>()
    val redoStack = mutableListOf<AbstractMove>()
    val undoStack = mutableListOf<AbstractMove>()

    var moveID = 0
    var intermezzoID = 0
    var isIntermezzo = false
    var intermezzoPlayerOrder: MutableList<Player> = mutableListOf()
    var lastRound = false
    var wildCardEnd = false

    /**
     * The current player.
     */
    val currentPlayer: Player
        get() = if (isIntermezzo) intermezzoPlayerOrder[intermezzoID] else players[moveID % players.size]

    /**
     * Checks if the end conditions of the game are met.
     * @return `true` if the end conditions are met, `false` otherwise.
     */
    val endConditionsMet: Boolean
        get() = drawPile.size < 5
                || players.size == 2 && players.any { it.score >= 75 }
                || players.size == 3 && players.any { it.score >= 60 }
                || players.size == 4 && players.any { it.score >= 45 }
}