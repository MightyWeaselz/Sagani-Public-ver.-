package service

import entity.Grid
import entity.PokaniGame
import entity.components.Position
import entity.components.Tile
import entity.move.AbstractPlaceMove
import entity.move.ai.Flip
import entity.move.ai.Satisfaction
import java.util.*

/**
 * Service of [Grid] related logic and Services such has [calculateTileChanges]
 * @param root for knowing the other services and using them via the [RootService]
 */
class GridService(private val root: RootService) : AbstractRefreshingService() {

    /**
     * Gathers all valid placements for a given [Grid].
     * @param grid the grid to check for valid placements
     * @return a list of valid positions
     */
    fun getValidPositions(grid: Grid) =
        if (grid.tiles.isEmpty()) {
            listOf(Position.ORIGIN)
        } else {
            grid.tiles.values.flatMap {
                it.position.adjacentPositions().filterNot { pos -> grid.tiles.containsKey(pos) }
            }
        }

    /**
     * Executes the changes of all tiles that are affected by
     * the placement in the grid of the current player.
     * @param tileToPlace the tile that was placed
     * @return a pair of the satisfactions and the deficit
     */
    fun calculateTileChanges(move: AbstractPlaceMove, tileToPlace: Tile) {
        val satisfactions = root.tileService.calculateSatisfactions(tileToPlace, move.player.grid)

        move.satisfactions.clear()
        move.satisfactions.addAll(satisfactions)

        val satisfactionPartition = satisfactions.partition { it.satisfiedTile == tileToPlace }

        move.player.grid.tiles[tileToPlace.position] = tileToPlace

        move.oldBoundingBox = move.player.grid.bb.copy()

        move.player.grid.bb.update(tileToPlace.position)

        // update satisfactions of other tiles
        satisfactionPartition.second.forEach { satisfaction ->
            updateSatisfaction(satisfaction).ifPresent {
                move.flips.add(it)
            }
        }

        move.player.currentDiscs -= tileToPlace.price

        move.deficit = if (move.player.currentDiscs < 0) {
            val def = -move.player.currentDiscs

            move.player.currentDiscs += def
            move.player.totalDiscs += def
            move.player.score -= def * 2

            def
        } else 0

        // update satisfactions of the placed tile
        satisfactionPartition.first.forEach { satisfaction ->
            updateSatisfaction(satisfaction).ifPresent {
                move.flips.add(it)
            }
        }
    }

    /**
     * Reverts the changes of all tiles that are affected by
     * the placement in the grid of the current player.
     * @param move the move to revert
     * @param game the game to update
     */
    fun revertTileChanges(move: AbstractPlaceMove, game: PokaniGame) {
        move.player.grid.tiles.remove(move.position)

        move.player.currentDiscs -= move.deficit
        move.player.currentDiscs += move.baseTileToPlace.price

        move.satisfactions.forEach {
            root.gridService.revertSatisfaction(it, game)
        }

        move.player.grid.bb = move.oldBoundingBox
    }

    /**
     * Updates the satisfaction of a given [Satisfaction] and flips the tile if all connections are satisfied.
     * @param satisfaction the satisfaction to update
     */
    private fun updateSatisfaction(satisfaction: Satisfaction): Optional<Flip> {
        satisfaction.connection.isSatisfied = true

//        println(
//            "Satisfied connection ${
//                satisfaction.connection.toActualRotationString(satisfaction.satisfiedTile.rotation)
//            } of tile ${satisfaction.satisfiedTile} with tile ${satisfaction.satisfactionCause}."
//        )

        if (satisfaction.satisfiedTile.isFlipped
            || satisfaction.satisfiedTile.connections.any { !it.isSatisfied }
        ) return Optional.empty()

        root.game.currentPlayer.lastScoreChangeMoveID = root.game.moveID
        flipTileUpdate(satisfaction.satisfiedTile)
        return Optional.of(Flip(satisfaction.satisfiedTile, satisfaction.satisfactionCause))
    }

    /**
     * Reverts the satisfaction of a given [Satisfaction] and flips the tile if all connections are satisfied.
     * @param satisfaction the satisfaction to update
     * @param game the game to update
     */
    private fun revertSatisfaction(satisfaction: Satisfaction, game: PokaniGame) {
        satisfaction.connection.isSatisfied = false

        if (!satisfaction.satisfiedTile.isFlipped) return

        game.currentPlayer.lastScoreChangeMoveID = game.moveID
        revertFlipTile(satisfaction.satisfiedTile)
    }

    /**
     * Flips the tile and updates the score and discs of the player
     * @param tile the tile to flip
     */
    private fun flipTileUpdate(tile: Tile) {
        tile.isFlipped = true
//        println("${root.game.currentPlayer} scored ${tile.value} points. Tile $tile is flipped.")
        root.game.currentPlayer.score += tile.value
        root.game.currentPlayer.currentDiscs += tile.price
    }

    /**
     * Reverts the flip of a tile and updates the score and discs of the player
     * @param tile the tile to flip
     */
    private fun revertFlipTile(tile: Tile) {
        tile.isFlipped = false
        root.game.currentPlayer.score -= tile.value
        root.game.currentPlayer.currentDiscs -= tile.price
    }
}