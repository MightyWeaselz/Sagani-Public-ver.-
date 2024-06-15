package entity.components

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import kotlinx.serialization.Serializable

/**
 * A [Connection] has a [rotation] and an [element]. Once a tile satisfies it, it becomes [isSatisfied].
 * @property rotation the [Rotation] of the connection
 * @property element the [Element] of the connection
 * @property isSatisfied whether the connection is satisfied or not
 */
@Serializable
data class Connection(
    val rotation: Rotation,
    val element: Element,
    var isSatisfied: Boolean = false
) {
    /**
     * @return a [String] representation of this [Connection] with the given [rotation]
     */
    fun toActualRotationString(rotation: Rotation): String {
        return Kolor.background(Kolor.foreground(this.rotation.rotate(rotation).symbol, Color.BLACK), element.color)
    }

    /**
     * Returns a string representation of the connection.
     * @return The string representation of the connection.
     */
    override fun toString() = "$element ${rotation.symbol}${if (isSatisfied) "✓" else ""}"
}