package entity.move.ai

import entity.components.Connection
import entity.components.Position
import entity.components.Tile
import kotlinx.serialization.Serializable

/**
 * An [Opportunity] is a [connection] of a [tile] that can be satisfied by another not yet placed tile at [position].
 * @property tile the tile that will be satisfied
 * @property connection the connection that will be satisfied
 * @property position the position where the tile will be placed
 */
@Serializable
data class Opportunity(val tile: Tile, val connection: Connection, val position: Position = Position.ORIGIN) {
    override fun toString() = "Another ${connection.element} tile at $position would satisfy ${
        connection.toActualRotationString(tile.rotation)
    } of tile $tile"
}