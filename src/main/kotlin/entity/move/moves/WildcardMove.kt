package entity.move.moves

import INTERMEZZO_SIZE
import entity.Player
import entity.components.BaseTile
import entity.components.Position
import entity.components.Rotation
import entity.move.AbstractPlaceMove
import kotlinx.serialization.Serializable
import service.RootService

/**
 * A [WildcardMove] is a specialized [AbstractPlaceMove] that is used to
 * place a tile that is taken directly from the draw pile without information about the connections.
 * @property position the position of the tile
 * @property rotation the rotation of the tile
 * @property baseTileToPlace the base tile to place
 * @property moveID the ID of the move
 * @property player the player that made the move
 */
@Serializable
class WildcardMove(
    override val position: Position,
    override val rotation: Rotation,
    override val baseTileToPlace: BaseTile,
    override val moveID: Int,
    override val player: Player
) : AbstractPlaceMove() {
    private lateinit var discardedOfferTile: BaseTile
    private var triggeredIntermezzo = false
    private var triggeredEndGame = false

    /**
     * Checks if the move is valid.
     */
    override fun check(root: RootService) {
        check(baseTileToPlace == root.game.drawPile.firstOrNull()) {
            throw InvalidBaseTileException(baseTileToPlace)
        }
    }

    /**
     * Executes the Wildcard specialized move and the changes
     * in the [WildcardMove] being made
     */
    override fun execute(root: RootService) {
        root.game.drawPile.remove(baseTileToPlace)
        discardedOfferTile = root.game.offerDisplay.removeFirst()
        root.game.intermezzoDisplay.add(discardedOfferTile)

        checkWildCardEnd(root)

        if (initiateIntermezzo(root)) return

        root.game.moveID++
    }

    /**
     * Undo the Wildcard specialized move and the changes
     * in the [WildcardMove] being made
     */
    override fun undo(root: RootService) {
        root.game.drawPile.add(0, baseTileToPlace)
        root.game.offerDisplay.add(0, discardedOfferTile)
        root.game.intermezzoDisplay.remove(discardedOfferTile)

        if (triggeredEndGame) {
            root.game.wildCardEnd = false
        }

        if (root.game.isIntermezzo) {
            root.game.isIntermezzo = false
            root.game.intermezzoPlayerOrder.clear()
            return
        }

        root.game.moveID--
    }

    private fun checkWildCardEnd(root: RootService) {
        if (root.game.drawPile.size > 4) return

        root.game.wildCardEnd = true
        triggeredEndGame = true
    }

    /**
     * Initiates the intermezzo if the intermezzo display is full.
     * @param root the root service
     * @return true if the intermezzo was initiated, false otherwise
     */
    private fun initiateIntermezzo(root: RootService) =
        if (root.game.intermezzoDisplay.size < INTERMEZZO_SIZE) false else {
            root.game.isIntermezzo = true
            triggeredIntermezzo = true
            root.game.intermezzoPlayerOrder.addAll(
                root.game.players.sortedWith(
                    compareBy<Player> { it.score }.thenBy { it.lastScoreChangeMoveID }
                )
            )
            true
        }


    override fun toString(): String {
        return super.toString() + "\n  Will discard: $discardedOfferTile${
            if (triggeredIntermezzo) {
                "\n  Will trigger Intermezzo."
            } else ""
        }"
    }
}