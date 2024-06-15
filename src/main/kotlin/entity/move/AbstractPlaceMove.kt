package entity.move

import entity.Grid
import entity.components.BaseTile
import entity.components.Position
import entity.components.Rotation
import entity.move.ai.Flip
import entity.move.ai.Opportunity
import entity.move.ai.Satisfaction
import kotlinx.serialization.Serializable
import service.RootService

/**
 * An [AbstractPlaceMove] is a specialized [AbstractMove] that is used to
 * place a tile in the game.
 * This implements the command pattern.
 * @property rotation the rotation of the tile
 * @property position the position of the tile
 * @property baseTileToPlace the base tile to place
 */
@Serializable
abstract class AbstractPlaceMove : AbstractMove() {
    abstract val rotation: Rotation
    abstract val position: Position
    abstract val baseTileToPlace: BaseTile

    val satisfactions: MutableSet<Satisfaction> = mutableSetOf()
    val flips: MutableSet<Flip> = mutableSetOf()
    val opportunities: MutableSet<Opportunity> = mutableSetOf()
    var deficit: Int = 0
    var oldBoundingBox = Grid.BoundingBox(0, 0, 0, 0)
    var populatedTiles = false

    /**
     * @return the tile to place
     */
    val toPlaceTile by lazy { baseTileToPlace.toTile(position, rotation) }

    /**
     * @return the total number of flips
     */
    val paybackValue by lazy { flips.sumOf { it.satisfiedTile.price } }

    /**
     * @return the number of satisfactions per coin
     */
    val satisfactionsPerDisc by lazy { satisfactions.size.toDouble() / toPlaceTile.price }

    /**
     * @return the distance to the origin
     */
    val distanceToOrigin by lazy { position.distanceTo(Position.ORIGIN) }


    /**
     * the overall Execute function that is checking some
     * rules that need to be fulfilled to execute the Move in itself
     * + executing it
     * @param root the root service to use other services
     */
    fun checkAndExecute(root: RootService) {
        check(rotation in Rotation.orthogonals()) {
            throw InvalidRotationException(rotation)
        }

        check(position in root.gridService.getValidPositions(player.grid)) {
            throw InvalidPositionException(position)
        }

        check(root)

        root.gridService.calculateTileChanges(this, toPlaceTile)

        execute(root)
    }

    /**
     * Inverse of the [checkAndExecute] function
     * @param root the root service to use other services
     */
    fun checkAndUndo(root: RootService) {
        undo(root)

        root.gridService.revertTileChanges(this, root.game)

        player.totalDiscs -= deficit
        player.score += deficit * 2
    }

    /**
     * @return a [String] representation of this [AbstractPlaceMove]
     */
    override fun toString() =
        "${this::class.simpleName}: Placement of base tile $baseTileToPlace as tile ${
            toPlaceTile.toString(false)
        } with rotation $rotation at (${toPlaceTile.position})${
            if (satisfactions.isNotEmpty()) {
                "\n  Satisfactions for the cost of ${
                    "%.2f".format(satisfactionsPerDisc)
                } satisfactions/coin: \n${
                    satisfactions.joinToString("\n") {
                        "    ${satisfactions.indexOf(it) + 1}: $it"
                    }
                }"
            } else ""
        }${
            if (flips.isNotEmpty()) {
                "\n  Flips: \n${
                    flips.joinToString("\n") {
                        "    ${flips.indexOf(it) + 1}: $it"
                    }
                }"
            } else ""
        }${
            if (opportunities.isNotEmpty()) {
                "\n  Opportunities: \n${
                    opportunities.joinToString("\n") {
                        "    ${opportunities.indexOf(it) + 1}: $it"
                    }
                }"
            } else ""
        }${
            if (deficit > 0) {
                "\n  Will cause a deficit of $deficit and a score damage of ${deficit * 2}."
            } else ""
        }${
            if (populatedTiles) {
                "\n  Will populate the offer display."
            } else ""
        }"

    /**
     * Thrown if the position is valid.
     * @param position the position that is not allowed to be occupied
     */
    class InvalidPositionException(position: Position) :
        Exception("Placing a tile at position $position is not considered a valid move.")

    /**
     * Thrown if the tile is not in the source set.
     * @param baseTile the base tile that is not in the source set
     */
    class InvalidBaseTileException(baseTile: BaseTile) : Exception("BaseTile $baseTile is not in the source set.")

    /**
     * Thrown if the rotation is not valid.
     * @param rotation the rotation that is not allowed
     */
    class InvalidRotationException(rotation: Rotation) : Exception("Invalid rotation $rotation")

    /**
     * Thrown if the move was not executed.
     */
    class MoveNotExecutedException : Exception("Cannot undo a move that was not executed.")
}