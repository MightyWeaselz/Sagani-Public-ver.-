package service

import edu.udo.cs.sopra.ntf.Color
import entity.Grid
import entity.Player
import entity.PlayerType
import entity.components.*
import entity.move.moves.PlaceMove
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * This class contains unit tests for the [GridService] class.
 */
class GridServiceTest {
    private val root = RootService()

    /**
     * Tests that updateBoundaries updates the boundaries correctly.
     */
    @Test
    fun updateBoundariesTest() {
        val grid = Grid()

        val tileToPlace = Tile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE)),
            Position.ORIGIN,
            Rotation.UP
        )

        grid.bb.update(tileToPlace.position)

        assertEquals(0, grid.bb.minX)
        assertEquals(0, grid.bb.maxX)
        assertEquals(0, grid.bb.minY)
        assertEquals(0, grid.bb.maxY)

        val tileToPlace2 = Tile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE)),
            Position(1, 1),
            Rotation.DOWN
        )

        grid.bb.update(tileToPlace2.position)

        assertEquals(0, grid.bb.minX)
        assertEquals(1, grid.bb.maxX)
        assertEquals(0, grid.bb.minY)
        assertEquals(1, grid.bb.maxY)
    }

    /**
     * Tests that getValidPositions returns a list of valid positions.
     */
    @Test
    fun getValidPositionsTest() {
        val grid = Grid()
        val tileToPlace = Tile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE)),
            Position.ORIGIN,
            Rotation.UP
        )

        val expectation = root.gridService.getValidPositions(grid)
        assertEquals(listOf(Position.ORIGIN), expectation)

        grid.tiles[Position.ORIGIN] = tileToPlace
        grid.bb.update(tileToPlace.position)

        val expectation2 = root.gridService.getValidPositions(grid)

        assertEquals(
            listOf(
                Position(0, 1),
                Position(1, 0),
                Position(0, -1),
                Position(-1, 0)
            ), expectation2
        )

        val tileToPlace2 = Tile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE)),
            Position(0, 1),
            Rotation.DOWN
        )

        grid.tiles[Position(0, 1)] = tileToPlace2
        grid.bb.update(tileToPlace2.position)

        val expectation3 = root.gridService.getValidPositions(grid)

        assertEquals(
            listOf(
                Position(1, 0),
                Position(0, -1),
                Position(-1, 0),
                Position(0, 2),
                Position(1, 1),
                Position(-1, 1)
            ), expectation3
        )
    }

    /**
     * Tests that calculateTileChanges calculates the correct changes.
     */
    @Test
    fun calculateTileChangesTest() {
        root.pokaniService.initializeGame(
            listOf(
                Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER),
                Player("Player 2", Color.BLACK, PlayerType.LOCAL_PLAYER)
            ), shuffled = false
        )

        val baseTileToPlace = BaseTile(
            0,
            Element.FIRE,
            mutableListOf(Connection(Rotation.UP, Element.FIRE))
        )

        val tileToPlace = baseTileToPlace.toTile(Position.ORIGIN, Rotation.UP)

        val move1 = PlaceMove(
            Position.ORIGIN,
            Rotation.UP,
            baseTileToPlace,
            root.game.moveID,
            root.game.currentPlayer
        )

        root.gridService.calculateTileChanges(move1, tileToPlace)

        assertEquals(root.game.currentPlayer.grid.tiles, mutableMapOf(Position.ORIGIN to tileToPlace))

        val tileToPlace2 = baseTileToPlace.toTile(Position(0, 1), Rotation.DOWN)

        val move2 = PlaceMove(
            Position(0, 1),
            Rotation.DOWN,
            baseTileToPlace,
            root.game.moveID,
            root.game.currentPlayer
        )

        root.gridService.calculateTileChanges(move2, tileToPlace2)

        val expectedGrid = mutableMapOf(Position.ORIGIN to tileToPlace, Position(0, 1) to tileToPlace2)

        assertEquals(root.game.currentPlayer.grid.tiles, expectedGrid)
        assertEquals(root.game.currentPlayer.score, 2)
        assertEquals(root.game.currentPlayer.currentDiscs, 24)
        assertEquals(root.game.currentPlayer.totalDiscs, 24)

        assertTrue(tileToPlace.isFlipped)
        assertTrue(tileToPlace.connections.all { it.isSatisfied })
        assertTrue(tileToPlace2.isFlipped)
        assertTrue(tileToPlace2.connections.all { it.isSatisfied })

        val tileToPlace3 = baseTileToPlace.toTile(Position(0, 2), Rotation.DOWN)

        val move3 = PlaceMove(
            Position(0, 2),
            Rotation.DOWN,
            baseTileToPlace,
            root.game.moveID,
            root.game.currentPlayer
        )

        root.game.currentPlayer.currentDiscs = 0
        root.gridService.calculateTileChanges(move3, tileToPlace3)

        val expectedGrid2 = mutableMapOf(
            Position.ORIGIN to tileToPlace,
            Position(0, 1) to tileToPlace2,
            Position(0, 2) to tileToPlace3
        )

        assertEquals(root.game.currentPlayer.grid.tiles, expectedGrid2)

        assertTrue(tileToPlace3.isFlipped)
        assertTrue(tileToPlace3.connections.all { it.isSatisfied })
    }

    @Test
    fun directionsSatisfiedTest() {
    }

    @Test
    fun allSatisfiedTrueTest() {
    }

    @Test
    fun allSatisfiedFalseTest() {
    }

    @Test
    fun maxDiscsAfterCacophonyTest() {
    }

    @Test
    fun flipTileUpdateScoreTest() {
    }

    @Test
    fun flipTileUpdateDiscsTest() {
    }

    @Test
    fun flipOwnTileUpdateTest() {
    }

    @Test
    fun ownDirectionsSatisfiedTest() {
    }
}