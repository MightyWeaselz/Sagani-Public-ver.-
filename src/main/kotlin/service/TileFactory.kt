package service

import ORIGINAL_TILES_PATH
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import entity.AbstractTile
import entity.components.BaseTile
import entity.components.Connection
import entity.components.Element
import entity.components.Rotation

/**
 * A [TileFactory] is a factory that creates [AbstractTile]s.
 */
object TileFactory {
    /**
     * @return a [Set] of [AbstractTile]s with [Element]s and [Connection]s from the original game.
     */
    fun getOriginalTiles(): Set<BaseTile> {
        val originalTiles = mutableSetOf<BaseTile>()

        csvReader().open("src/main/resources/$ORIGINAL_TILES_PATH") {
            readAllAsSequence().forEachIndexed { index, row ->
                if (index == 0) return@forEachIndexed

                val connections = Rotation.values().mapNotNull { direction ->
                    val csvDirection = direction.ordinal + 2
                    val csvValue = row[csvDirection]

                    if (csvValue != "NONE") {
                        Connection(direction, Element.valueOf(csvValue))
                    } else {
                        null
                    }
                }.toMutableList()

                originalTiles.add(
                    BaseTile(row[0].toInt(), Element.valueOf(row[1]), connections)
                )
            }
        }

        return originalTiles
    }
}