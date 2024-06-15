package entity.components

import kotlinx.serialization.Serializable

/**
 * A [Rotation] is a direction in which a [Tile] or a [Connection] can be rotated.
 * @property dx the x offset of the rotation
 * @property dy the y offset of the rotation
 * @property symbol the symbol of the rotation
 */
@Serializable
enum class Rotation(val dx: Int, val dy: Int, val symbol: String) {
    UP(0, 1, "↑"),
    UP_RIGHT(1, 1, "↗"),
    RIGHT(1, 0, "→"),
    DOWN_RIGHT(1, -1, "↘"),
    DOWN(0, -1, "↓"),
    DOWN_LEFT(-1, -1, "↙"),
    LEFT(-1, 0, "←"),
    UP_LEFT(-1, 1, "↖");

    /**
     * @return the [Rotation] that is the result of rotating this [Rotation] by the given [rotation]
     */
    fun rotate(rotation: Rotation) = Rotation.values()[(ordinal + rotation.ordinal).mod(8)]

    /**
     * @return the opposite [Rotation] of this [Rotation].
     */
    fun opposite() = Rotation.values()[(ordinal + 4).mod(8)]

    /**
     * @return a string representation of the rotation.
     */
    override fun toString() = symbol

    companion object {
        /**
         * @return a list of all [Rotation]s that are orthogonal
         */
        fun orthogonals() = listOf(
            UP,
            RIGHT,
            DOWN,
            LEFT
        )
    }
}