package entity.move

import INTERMEZZO_SIZE
import entity.Player
import entity.components.BaseTile
import kotlinx.serialization.Serializable
import service.RootService

/**
 * An [AbstractMove] is an abstract class that is used to
 * represent a move in the game.
 * @property moveID the ID of the move
 * @property player the player that made the move
 */
@Serializable
abstract class AbstractMove {
    abstract val moveID: Int
    abstract val player: Player
    val oldSelection = mutableListOf<BaseTile>()
    val oldDiscardPile = mutableListOf<BaseTile>()

    /**
     * Checks if the move is valid.
     */
    abstract fun check(root: RootService)

    /**
     * Executes the move.
     */
    abstract fun execute(root: RootService)

    /**
     * Undoes the move.
     */
    abstract fun undo(root: RootService)

    /**
     * Checks if the intermezzo phase has ended.
     */
    fun endIntermezzo(root: RootService) {
        if (root.game.intermezzoID < root.game.players.size || !root.game.isIntermezzo) return

        root.game.isIntermezzo = false
        root.game.intermezzoID = 0
        root.pokaniService.populateDisplay()

        // Put cards from intermezzoDisplay to discardPile
        if (root.game.intermezzoDisplay.size == INTERMEZZO_SIZE) {
            oldDiscardPile.clear()
            oldDiscardPile.addAll(root.game.discardPile)
            root.game.discardPile.addAll(root.game.intermezzoDisplay)
            root.game.intermezzoDisplay.clear()
        }

        root.game.moveID++
    }

    /**
     * Undoes the end of the intermezzo phase.
     */
    fun undoEndIntermezzo(root: RootService) {
        if (root.game.isIntermezzo) return

        root.game.discardPile.clear()
        root.game.discardPile.addAll(oldDiscardPile)
        root.game.intermezzoDisplay.addAll(oldSelection)
        root.game.isIntermezzo = true
        root.pokaniService.revertDisplay()
        root.game.intermezzoID = root.game.players.size
        root.game.moveID--
    }

    /**
     * Exception to be thrown when an illegal move is made
     */
    class InvalidMoveException(move: AbstractMove) : Exception("The move is not allowed: $move")
}