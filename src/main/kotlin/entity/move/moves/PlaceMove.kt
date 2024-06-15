package entity.move.moves

import entity.Player
import entity.components.BaseTile
import entity.components.Position
import entity.components.Rotation
import entity.move.AbstractPlaceMove
import kotlinx.serialization.Serializable
import service.RootService

/**
 * A [PlaceMove] is a specialized [AbstractPlaceMove] that is used to
 * place a tile in the game.
 * @property position the position of the tile
 * @property rotation the rotation of the tile
 * @property baseTileToPlace the base tile to place
 * @property moveID the ID of the move
 * @property player the player that made the move
 */
@Serializable
class PlaceMove(
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
        check(baseTileToPlace in root.game.offerDisplay) {
            throw InvalidBaseTileException(baseTileToPlace)
        }
    }

    /**
     * Executes the Place specialized move and the changes
     * in the [PlaceMove] being made
     */
    override fun execute(root: RootService) {
        oldSelection.clear()
        oldSelection.addAll(root.game.offerDisplay)
        root.game.offerDisplay.remove(baseTileToPlace)
        root.game.moveID++
    }

    /**
     * Undo the Place specialized move and the changes
     * in the [PlaceMove] being made
     */
    override fun undo(root: RootService) {
        root.game.offerDisplay.clear()
        root.game.offerDisplay.addAll(oldSelection)
        root.game.moveID--
    }
}