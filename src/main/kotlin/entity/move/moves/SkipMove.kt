package entity.move.moves

import entity.Player
import entity.move.AbstractMove
import kotlinx.serialization.Serializable
import service.RootService

/**
 * A [SkipMove] is a specialized [AbstractMove] that is used to
 * skip a turn in the game while the intermezzo phase is active.
 * @property moveID the ID of the move
 * @property player the player that made the move
 */
@Serializable
class SkipMove(
    override val moveID: Int,
    override val player: Player
) : AbstractMove() {

    /**
     * Checks if the move is valid.
     */
    override fun check(root: RootService) {
        check(root.game.isIntermezzo) {
            throw InvalidMoveException(this)
        }
    }

    /**
     * Executes the Skip specialized move and the changes
     * in the [SkipMove] being made
     */
    override fun execute(root: RootService) {
        oldSelection.clear()
        oldSelection.addAll(root.game.intermezzoDisplay)
        root.game.intermezzoID++

        endIntermezzo(root)
    }

    /**
     * Undo the Skip specialized move and the changes
     * in the [SkipMove] being made
     */
    override fun undo(root: RootService) {
        undoEndIntermezzo(root)

        root.game.intermezzoID--
    }

    override fun toString() = "SkipMove"
}