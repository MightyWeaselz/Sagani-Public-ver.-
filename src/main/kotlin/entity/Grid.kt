package entity

import com.andreapivetta.kolor.Color
import com.andreapivetta.kolor.Kolor
import entity.components.Position
import entity.components.Tile
import kotlinx.serialization.Serializable


/**
 * A [Grid] is a collection of [Tile]s that will be updated based on the game rules on every placement.
 */
@Serializable
class Grid {
    val tiles: MutableMap<Position, Tile> = mutableMapOf()

    var bb = BoundingBox(0, 0, 0, 0)

    /**
     * Returns a tile at a specific position
     * @param position which defines where the tile is placed.
     */
    fun getTileAt(position: Position) = tiles[position]

    fun toString(validPositions: List<Position>, player: Player): String {
        val string = StringBuilder()
        val sizeFormatted = "${(bb.minX..bb.maxX).count()}x${(bb.minY..bb.maxY).count()}"

        string.append(
            "\n$player with ${tiles.size} tiles in grid $sizeFormatted. " +
                    "${validPositions.size} position(s) open"
        )

        (bb.minY - 2..bb.maxY + 2).reversed().forEach { y ->
            string.appendLine()

            (bb.minX - 2..bb.maxX + 2).forEach inner@{ x ->
                val pos = Position(x, y)

                val tile = getTileAt(pos)
                if (tile != null) {
                    string.append(tile.toString(true))
                    return@inner
                }

                val backGround = if (pos in validPositions) {
                    Color.DARK_GRAY
                } else {
                    Color.LIGHT_GRAY
                }

                if (pos in validPositions) {
                    string.append(
                        Kolor.background(
                            "${validPositions.indexOf(pos) + 1}".padEnd(5, ' '),
                            backGround
                        )
                    )
                    return@inner
                }

                string.append(Kolor.background("     ", backGround))
            }
        }

        string.appendLine()
        return string.toString()
    }

    /**
     * stuff
     */
    @Serializable
    data class BoundingBox(var minX: Int, var maxX: Int, var minY: Int, var maxY: Int) {

        /**
         * Updates the boundaries after a tile is place in [executeMove]
         * @param position that is used to update the boundaries
         */
        fun update(position: Position) {
            if (position.x < minX) {
                minX = position.x
            }
            if (position.x > maxX) {
                maxX = position.x
            }
            if (position.y < minY) {
                minY = position.y
            }
            if (position.y > maxY) {
                maxY = position.y
            }
        }
    }
}

