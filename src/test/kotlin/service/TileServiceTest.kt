package service

import entity.Grid
import entity.components.*
import entity.move.ai.Satisfaction
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TileServiceTest {
    private val root = RootService()

    @Test
    fun calculateSatisfactionsTest() {
        val grid = Grid()
        val tileToPlace = Tile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE)),
            Position.ORIGIN,
            Rotation.UP
        )

        assertEquals(root.tileService.calculateSatisfactions(tileToPlace, grid), emptySet())

        grid.tiles[Position.ORIGIN] = tileToPlace
        grid.bb.update(tileToPlace.position)

        val tileToPlace2 = Tile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE)),
            Position(0, 1),
            Rotation.DOWN
        )

        assertEquals(
            root.tileService.calculateSatisfactions(tileToPlace2, grid), setOf(
                Satisfaction(
                    tileToPlace,
                    tileToPlace2,
                    Connection(Rotation.UP, Element.FIRE)
                ),
                Satisfaction(
                    tileToPlace2,
                    tileToPlace,
                    Connection(Rotation.UP, Element.FIRE)
                )
            )
        )
    }

    @Test
    fun getTilesInDirectionTest() {
        val grid = Grid()
        val tileToPlace = Tile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE)),
            Position.ORIGIN,
            Rotation.UP
        )

        assertEquals(emptyList(), root.tileService.getTilesInDirection(tileToPlace, grid, Rotation.UP))

        grid.tiles[Position.ORIGIN] = tileToPlace
        grid.bb.update(tileToPlace.position)

        val tileToPlace2 = Tile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE)),
            Position(0, 1),
            Rotation.DOWN
        )

        grid.tiles[Position(0, 1)] = tileToPlace2
        grid.bb.update(tileToPlace2.position)

        assertEquals(listOf(tileToPlace), root.tileService.getTilesInDirection(tileToPlace2, grid, Rotation.DOWN))
        assertEquals(listOf(tileToPlace2), root.tileService.getTilesInDirection(tileToPlace, grid, Rotation.UP))
    }
}