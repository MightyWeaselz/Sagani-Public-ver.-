package entity.components

import entity.Grid
import kotlinx.serialization.Serializable
import kotlin.math.hypot

/**
 * A [Position] is a position on a [Grid].
 * @property x the x coordinate of the position
 * @property y the y coordinate of the position
 */
@Serializable
data class Position(
    val x: Int,
    val y: Int
) {
    /**
     * @return a [Position] that is offset in the direction of the given [Rotation]
     */
    fun offset(other: Rotation) = Position(x + other.dx, y + other.dy)

    /**
     * @return a [Position] that is offset in the direction of the given [Rotation] by the given [amount]
     */
    fun offset(other: Rotation, amount: Int) = Position(x + other.dx * amount, y + other.dy * amount)

    /**
     * @return the distance from self to the given [Position]
     */
    fun distanceTo(other: Position) = hypot((x - other.x).toDouble(), (y - other.y).toDouble()).toInt()

    /**
     * @return whether this [Position] is in the given [Grid.BoundingBox]
     */
    fun isInBounds(boundingBox: Grid.BoundingBox) =
        x in boundingBox.minX..boundingBox.maxX
                && y in boundingBox.minY..boundingBox.maxY

    /**
     * @return a [List] of [Position]s that are adjacent to this [Position]
     */
    fun adjacentPositions() = Rotation.orthogonals().map { offset(it) }

    /**
     * Returns a string representation of the Position.
     * @return a string with the [x] and [y] position.
     */
    override fun toString() = "${x}x$y"

    companion object {
        /**
         * The origin [Position] (0, 0)
         */
        val ORIGIN = Position(0, 0)
    }
}