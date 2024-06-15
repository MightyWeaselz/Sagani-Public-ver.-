package entity.move.moves

import entity.Player
import entity.components.BaseTile
import entity.components.Position
import entity.components.Rotation
import entity.move.AbstractPlaceMove
import kotlinx.serialization.Serializable
import service.RootService

/**
 * An [IntermezzoMove] is a specialized [AbstractPlaceMove] that is used to
 * place a tile in the intermezzo phase of the game.
 * @property position the position of the tile
 * @property rotation the rotation of the tile
 * @property baseTileToPlace the base tile to place
 * @property moveID the ID of the move
 * @property player the player that made the move
 * @property oldDiscardPile the old discard pile
 */
@Serializable
class IntermezzoMove(
    override val position: Position,
    override val rotation: Rotation,
    override val baseTileToPlace: BaseTile,
    override val moveID: Int,
    override val player: Player
) : AbstractPlaceMove() {

    /**
     * Checks if the move is valid.
     */
    override fun check(root: RootService) {
        check(baseTileToPlace in root.game.intermezzoDisplay) {
            throw InvalidBaseTileException(baseTileToPlace)
        }

        check(root.game.isIntermezzo) {
            throw InvalidMoveException(this)
        }
    }

    /**
     * Executes the Intermezzo specialized move and the changes
     * in the [IntermezzoMove] being made
     */
    override fun execute(root: RootService) {
        oldSelection.clear()
        oldSelection.addAll(root.game.intermezzoDisplay)
        root.game.intermezzoDisplay.remove(baseTileToPlace)
        root.game.intermezzoID++

        endIntermezzo(root)
    }

    /**
     * Undo the Intermezzo specialized move and the changes
     * in the [IntermezzoMove] being made
     */
    override fun undo(root: RootService) {
        undoEndIntermezzo(root)

        root.game.intermezzoDisplay.clear()
        root.game.intermezzoDisplay.addAll(oldSelection)
        root.game.intermezzoID--
    }
}