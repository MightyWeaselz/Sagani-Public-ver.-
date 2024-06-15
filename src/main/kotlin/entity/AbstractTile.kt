package entity

import MAX_CONNECTIONS
import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import entity.components.*
import kotlinx.serialization.Serializable

/**
 * An [AbstractTile] is a tile that is not yet placed on a [Grid].
 * It has [connections] that can be satisfied by other tiles.
 * @property element the [Element] of the tile
 * @property connections the [Connection]s of the tile
 */
@Serializable
abstract class AbstractTile {
    abstract val tileID: Int
    abstract val element: Element
    abstract val connections: List<Connection>

    /**
     * @return the game points of this [AbstractTile] based on the amount of [connections]
     */
    val value: Int
        get() = when (price) {
            1 -> 1
            2 -> 3
            3 -> 6
            else -> 10
        }

    /**
     * @return the price of this [AbstractTile] based on the amount of [connections]
     */
    val price: Int
        get() = connections.size

    /**
     * @return a [Tile] with the given [position] and [rotation]
     */
    fun toTile(position: Position, rotation: Rotation) = Tile(tileID, element, connections, position, rotation)

    /**
     * Returns a string representation the [AbstractTile].
     * @return a [String] with the [element], all [connections] and their rotation symbol from a [AbstractTile]
     */
    override fun toString(): String {
        val connectionsPrint = StringBuilder()

        connectionsPrint.append(Kolor.background(" ", element.color))

        connections.forEach {
            connectionsPrint.append(it.toActualRotationString(Rotation.UP))
        }

        if (price < MAX_CONNECTIONS) {
            val dotFill = "·".repeat(MAX_CONNECTIONS - price)

            connectionsPrint.append(Kolor.background(Kolor.foreground(dotFill, Color.BLACK), element.color))
        }

        return "$connectionsPrint"
    }
}
