package entity.components

import com.andreapivetta.kolor.Color
import kotlinx.serialization.Serializable

/**
 * An [Element] is a type of [Tile] or [Connection] that can be placed on a [Grid].
 * @property color the [Color] of the element
 */
@Serializable
enum class Element(val color: Color) {
    FIRE(Color.RED),
    WATER(Color.LIGHT_BLUE),
    EARTH(Color.LIGHT_GREEN),
    AIR(Color.WHITE)
}