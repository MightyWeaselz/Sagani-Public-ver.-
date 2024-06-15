package service

import entity.Grid
import entity.components.Rotation
import entity.components.Tile
import entity.move.ai.Opportunity
import entity.move.ai.Satisfaction

/**
 * [TileService] contains all functions a tile needs to calculate if its connections are satisfied.
 */
class TileService : AbstractRefreshingService() {

    /**
     * Returns a set of [Satisfaction]s that are satisfied by the placed [Tile].
     * @param toBePlacedTile contains the newly placed tile
     * @param grid contains the grid on which the tile has been placed
     */
    fun calculateSatisfactions(toBePlacedTile: Tile, grid: Grid): Set<Satisfaction> {
        val satisfactions = mutableSetOf<Satisfaction>()

        Rotation.values().forEach { rotation ->
            val tilesInDirection = getTilesInDirection(toBePlacedTile, grid, rotation)

            /* Check if the connection of placed tile is satisfied */
            toBePlacedTile.connections.firstOrNull { it.rotation.rotate(toBePlacedTile.rotation) == rotation }
                ?.let { ownConnection ->
                    tilesInDirection.firstOrNull { it.element == ownConnection.element }?.let { tile ->
                        satisfactions.add(Satisfaction(toBePlacedTile, tile, ownConnection))
                    }
                }

            /* Check in each direction if tiles of the grid are satisfied */
            tilesInDirection
                .filterNot { it.isFlipped }
                .forEach { tileInDirection ->
                    tileInDirection.connections
                        .filterNot { it.isSatisfied }
                        .firstOrNull {
                            it.rotation.rotate(tileInDirection.rotation)
                                .opposite() == rotation && it.element == toBePlacedTile.element
                        }?.let {
                            satisfactions.add(Satisfaction(tileInDirection, toBePlacedTile, it))
                        }
                }
        }

        return satisfactions
    }

    /**
     * Returns a set of [Opportunity]s that are given on the current grid.
     * @param grid the grid on which the opportunities are calculated
     */
    fun calculateOpportunities(grid: Grid): Set<Opportunity> {
        return emptySet()
    }

    /**
     * The function returns a list which contains the tiles of a direction
     * @param tile contains the tile from which to start
     * @param grid contains the grid on which the tile has been placed
     * @param rotation contains the direction into which the grid is traversed
     */
    fun getTilesInDirection(tile: Tile, grid: Grid, rotation: Rotation): List<Tile> {
        val tilesInDirection = mutableListOf<Tile>()
        var currentPosition = tile.position.offset(rotation)

        while (currentPosition.isInBounds(grid.bb)) {
            grid.getTileAt(currentPosition)?.let {
                tilesInDirection.add(it)
            }
            currentPosition = currentPosition.offset(rotation)
        }

        return tilesInDirection
    }
}