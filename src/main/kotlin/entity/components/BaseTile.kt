package entity.components

import entity.AbstractTile
import entity.Grid
import kotlinx.serialization.Serializable

/**
 * A [BaseTile] is a [AbstractTile] that can be later placed on a [Grid]
 * once a position and a rotation was provided.
 * It has [connections] that can be satisfied by other tiles.
 * @property tileID the ID of the tile
 * @property element the [Element] of the tile
 * @property connections the [Connection]s of the tile
 */
@Serializable
data class BaseTile(
    override val tileID: Int,
    override val element: Element,
    override val connections: List<Connection>
) : AbstractTile() {
    override fun toString() = super.toString()
}