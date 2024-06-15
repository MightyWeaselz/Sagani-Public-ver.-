package entity.move.ai

import entity.components.Connection
import entity.components.Tile
import kotlinx.serialization.Serializable

/**
 * A [Satisfaction] is a [connection] of a [satisfiedTile] that can be satisfied by a concrete [satisfactionCause].
 * @property satisfiedTile the tile that will be satisfied
 * @property satisfactionCause the tile that will satisfy
 * @property connection the connection that will be satisfied
 */
@Serializable
data class Satisfaction(val satisfiedTile: Tile, val satisfactionCause: Tile, val connection: Connection) {
    override fun toString() = "${
        satisfactionCause.toString(false)
    } at ${
        satisfactionCause.position
    } would satisfy the connection ${
        connection.toActualRotationString(satisfiedTile.rotation)
    } of ${
        satisfiedTile.toString(false)
    }"
}