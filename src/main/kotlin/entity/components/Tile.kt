package entity.components

import MAX_CONNECTIONS
import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import entity.AbstractTile
import entity.Grid
import kotlinx.serialization.Serializable

/**
 * A [Tile] is a [AbstractTile] that can be placed on a [Grid].
 * It has [connections] that can be satisfied by other tiles.
 * @property tileID the ID of the tile
 * @property element the [Element] of the tile
 * @property connections the [Connection]s of the tile
 * @property position the [Position] of the tile
 * @property rotation the [Rotation] of the tile
 * @property isFlipped whether the tile is satisfied or not
 */
@Serializable
data class Tile(
    override val tileID: Int,
    override val element: Element,
    override val connections: List<Connection>,
    var position: Position,
    var rotation: Rotation
) : AbstractTile() {
    var isFlipped: Boolean = false

    fun toString(showFlip: Boolean): String {
        if (showFlip && isFlipped) {
            return Kolor.background(Kolor.foreground("$value".padEnd(5, ' '), Color.BLACK), element.color)
        }

        val formattedConnectionCount =
            Kolor.background(Kolor.foreground(price.toString(), Color.BLACK), element.color)

        val connectionsPrint = StringBuilder()

        connections.forEach {
            if (it.isSatisfied) {
                val innerString = Kolor.foreground(it.rotation.rotate(rotation).symbol, it.element.color)
                connectionsPrint.append(Kolor.background(innerString, Color.BLACK))
            } else {
                val innerString = Kolor.foreground(it.rotation.rotate(rotation).symbol, Color.BLACK)
                connectionsPrint.append(Kolor.background(innerString, it.element.color))
            }
        }

        if (price < MAX_CONNECTIONS) {
            val filler = "·".repeat(MAX_CONNECTIONS - price)

            connectionsPrint.append(Kolor.background(Kolor.foreground(filler, Color.BLACK), element.color))
        }

        return "$formattedConnectionCount$connectionsPrint"
    }
}