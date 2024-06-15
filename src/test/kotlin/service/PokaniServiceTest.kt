package service

import INTERMEZZO_SIZE
import OFFER_SIZE
import edu.udo.cs.sopra.ntf.Color
import entity.Player
import entity.PlayerType
import entity.PokaniGame
import entity.components.Position
import entity.components.Rotation
import entity.move.moves.IntermezzoMove
import entity.move.moves.PlaceMove
import entity.move.moves.SkipMove
import entity.move.moves.WildcardMove
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * This class contains unit tests for the [PokaniService] class.
 */

class PokaniServiceTest {

    /**
     * Tests that initializeGame throws InvalidPlayerCountException
     * when the player count is less than 2.
     * When a player list contains more than 4 players,
     * initializeGame should throw PlayerNameNotValidException
     * when player name is empty or consists of whitespace characters
     * */
    @Test
    fun initializeGameTest() {
        val root = RootService()

        assertThrows<PokaniService.InvalidPlayerCountException> {
            root.pokaniService.initializeGame(emptyList()) // Test with an empty player list
        }

        assertThrows<PokaniService.InvalidPlayerCountException> {
            val players = listOf(Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER))
            root.pokaniService.initializeGame(players) // Test with a player list containing only one player
        }

        assertThrows<PokaniService.InvalidPlayerCountException> {
            val players = listOf(
                Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER),
                Player("Player 2", Color.BLACK, PlayerType.LOCAL_PLAYER),
                Player("Player 3", Color.BLACK, PlayerType.LOCAL_PLAYER),
                Player("Player 4", Color.GREY, PlayerType.LOCAL_PLAYER),
                Player("Player 5", Color.BROWN, PlayerType.LOCAL_PLAYER)
            )
            root.pokaniService.initializeGame(players) // Test with a player list containing more than 4 players
        }

        assertThrows<PokaniService.PlayerNameNotValidException> {
            val players = listOf(
                Player("", Color.WHITE, PlayerType.LOCAL_PLAYER),
                Player("   ", Color.BLACK, PlayerType.LOCAL_PLAYER),
                Player("Player 3", Color.BLACK, PlayerType.LOCAL_PLAYER),
            )
            root.pokaniService.verifyPlayerData(players[0].name, players[0].color, players[0].playerType)
            root.pokaniService.verifyPlayerData(players[1].name, players[1].color, players[1].playerType)
        }
    }

    /**
     * Tests that populateDrawPile populates the draw pile with tiles.
     */
    @Test
    fun populateDrawPileTest() {
        val root = RootService()

        val game = PokaniGame(listOf(Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER)))
        root.game = game

        root.pokaniService.populateDrawPile()

        val expectedTilesCount = TileFactory.getOriginalTiles().size
        val actualTilesCount = game.drawPile.size

        assertEquals(expectedTilesCount, actualTilesCount)
    }

    /**
     * Tests that populateDisplay populates the display with 5 cards.
     */
    @Test
    fun populateDisplayTest() {
        val root = RootService()

        val game = PokaniGame(listOf(Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER)))
        root.game = game

        root.pokaniService.populateDrawPile()
        root.pokaniService.populateDisplay()

        val actualDisplaySize = game.offerDisplay.size

        assertEquals(OFFER_SIZE, actualDisplaySize)
    }

    /**
     * Tests that executeMove throws InvalidMoveException
     * when the move is not valid.
     */
    @Test
    fun executeMoveTest() {
        val root = RootService()

        val playerOne = Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER)
        val playerTwo = Player("Player 2", Color.BLACK, PlayerType.LOCAL_PLAYER)

        root.pokaniService.initializeGame(
            listOf(playerOne, playerTwo),
            shuffled = false
        )

        val tileToPlace = root.game.offerDisplay.firstOrNull() ?: return

        val move = PlaceMove(
            Position.ORIGIN,
            Rotation.UP,
            tileToPlace,
            root.game.moveID,
            root.game.currentPlayer
        )

        root.pokaniService.executeMove(move)

        assertEquals(playerTwo, root.game.currentPlayer)
        assertEquals(1, root.game.moveID)
        assertEquals(OFFER_SIZE - 1, root.game.offerDisplay.size)
        assertEquals(0, playerOne.score)
        assertEquals(24 - tileToPlace.connections.size, playerOne.currentDiscs)
        assertEquals(false, root.game.lastRound)

        root.pokaniService.undoMove()

        assertEquals(playerOne, root.game.currentPlayer)
        assertEquals(0, root.game.moveID)
        assertEquals(OFFER_SIZE, root.game.offerDisplay.size)
        assertEquals(0, playerOne.score)
        assertEquals(24, playerOne.currentDiscs)
    }

    /**
     * Tests if the endConditions trigger correctly
     */
    @Test
    fun endConditionsTest() {
        val root = RootService()
        val playerOne = Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER)
        val playerTwo = Player("Player 2", Color.BLACK, PlayerType.LOCAL_PLAYER)

        root.pokaniService.initializeGame(
            listOf(playerOne, playerTwo),
            shuffled = false
        )

        repeat(root.game.drawPile.size - 5) {
            root.game.discardPile.add(root.game.drawPile.removeFirst())
        }

        repeat(4) {
            root.game.discardPile.add(root.game.offerDisplay.removeLast())
        }

        assertEquals(1, root.game.offerDisplay.size)
        assertEquals(5, root.game.drawPile.size)

        val tileToPlace = root.game.drawPile.firstOrNull() ?: return

        val wildMove = WildcardMove(
            Position.ORIGIN,
            Rotation.UP,
            tileToPlace,
            root.game.moveID,
            root.game.currentPlayer
        )

        println(root.game.offerDisplay.size)

        root.pokaniService.executeMove(wildMove)

        println(root.game.offerDisplay.size)
        println(root.game.drawPile.size)


        assertEquals(true, root.game.wildCardEnd)
        assertEquals(true, root.game.lastRound)

        root.pokaniService.undoMove()

        assertEquals(false, root.game.wildCardEnd)
        assertEquals(false, root.game.lastRound)

    }

    /**
     * Tests for performIntermezzo if it is executed and the playerOrder is correct calculated
     */
    @Test
    fun intermezzoErrorTest() {
        val root = RootService()

        val playerOne = Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER)
        val playerTwo = Player("Player 2", Color.BLACK, PlayerType.LOCAL_PLAYER)

        // order should be ascending by score
        playerOne.score = 10
        playerTwo.score = 15
        playerOne.lastScoreChangeMoveID = 15
        playerTwo.lastScoreChangeMoveID = 15

        root.pokaniService.initializeGame(
            listOf(playerOne, playerTwo),
            shuffled = false
        )

        //IntermezzoDisplay fill with 3 tiles
        val draws = root.game.drawPile.take(INTERMEZZO_SIZE - 1)
        root.game.intermezzoDisplay.addAll(draws)
        root.game.drawPile.removeAll(draws)

        /*
        * Test if intermezzo is executed, when intermezzoDisplay has 4 tiles
        */
        val tileToPlace = root.game.drawPile.firstOrNull() ?: return

        val wildMove = WildcardMove(
            Position.ORIGIN,
            Rotation.UP,
            tileToPlace,
            root.game.moveID,
            root.game.currentPlayer
        )

        // Test if isIntermezzo set by WildcardMove
        root.pokaniService.executeMove(wildMove)
        assertTrue(root.game.isIntermezzo)
        assertEquals(playerOne, root.game.intermezzoPlayerOrder[0])

        // Test if Intermezzo is undone
        root.pokaniService.undoMove()
        assertFalse(root.game.isIntermezzo)
        assertEquals(root.game.players[root.game.moveID % root.game.players.size], root.game.currentPlayer)
        assertEquals(3, root.game.intermezzoDisplay.size)

        // Test if the playerOrder in Intermezzo is correct sorted, order should be ascending by lastChange
        playerOne.score = 15

        playerOne.lastScoreChangeMoveID = 10
        playerTwo.lastScoreChangeMoveID = 15

        root.pokaniService.executeMove(wildMove)
        assertEquals(playerOne, root.game.currentPlayer)

        val skipMove = SkipMove(
            root.game.moveID,
            root.game.currentPlayer
        )

        // Test if skip works - intermezzoId increased and moveID still the same
        root.pokaniService.executeMove(skipMove)
        assertEquals(playerTwo, root.game.currentPlayer)
        assertEquals(1, root.game.intermezzoID)
        assertEquals(0, root.game.moveID)

        // Test if skip is undone
        root.pokaniService.undoMove()
        assertEquals(playerOne, root.game.currentPlayer)
        assertEquals(0, root.game.intermezzoID)
        assertEquals(0, root.game.moveID)

    }

    /**
     * Tests for performIntermezzo throw InvalidMoveException
     * when the move is not valid.
     */
    @Test
    fun performIntermezzo() {
        val root = RootService()

        val playerOne = Player("Player 1", Color.WHITE, PlayerType.LOCAL_PLAYER)
        val playerTwo = Player("Player 2", Color.BLACK, PlayerType.LOCAL_PLAYER)

        playerOne.score = 10
        playerTwo.score = 15
        playerOne.lastScoreChangeMoveID = 15
        playerTwo.lastScoreChangeMoveID = 15

        root.pokaniService.initializeGame(
            listOf(playerOne, playerTwo),
            shuffled = false
        )


        // IntermezzoDisplay fill with 3 tiles
        val draws = root.game.drawPile.take(INTERMEZZO_SIZE - 1)
        root.game.intermezzoDisplay.addAll(draws)
        root.game.drawPile.removeAll(draws)

        /*
        * Test if intermezzo is executed, when intermezzoDisplay has 4 tiles
        */
        var tileToPlace = root.game.drawPile.firstOrNull() ?: return

        val move = WildcardMove(
            Position.ORIGIN,
            Rotation.UP,
            tileToPlace,
            root.game.moveID,
            root.game.currentPlayer
        )

        root.pokaniService.executeMove(move)

        // Test Intermezzo
        tileToPlace = root.game.intermezzoDisplay[0]

        val intermezzoMove = IntermezzoMove(
            Position(0, 1),
            Rotation.UP,
            tileToPlace,
            root.game.moveID,
            root.game.currentPlayer
        )

        root.pokaniService.executeMove(intermezzoMove)

        assertEquals(playerTwo, root.game.currentPlayer)
        assertEquals(intermezzoMove.toPlaceTile, root.game.intermezzoPlayerOrder[0].grid.getTileAt(Position(0, 1)))
        assertEquals(1, root.game.intermezzoID)
        assertEquals(INTERMEZZO_SIZE - 1, root.game.intermezzoDisplay.size)

        root.pokaniService.undoMove()

        assertEquals(0, root.game.intermezzoID)
        assertEquals(INTERMEZZO_SIZE, root.game.intermezzoDisplay.size)
        assertEquals(playerOne, root.game.currentPlayer)
        assertEquals(20, root.game.intermezzoPlayerOrder[0].currentDiscs)

        // Test if skip works - intermezzoId increased and moveID still the same
        repeat(2) { root.pokaniService.executeMove(SkipMove(root.game.moveID, root.game.currentPlayer)) }
        assertEquals(INTERMEZZO_SIZE, root.game.discardPile.size)
        assertFalse(root.game.isIntermezzo)

        // test discard undo
        root.pokaniService.undoMove()

        assertEquals(0, root.game.discardPile.size)
        assertEquals(4, root.game.intermezzoDisplay.size)
        assertTrue(root.game.isIntermezzo)
    }

    @Test
    fun testUndoRedo() {
        val root = RootService()

        val playerOne = Player("Alice", Color.WHITE, PlayerType.SMART_BOT)
        val playerTwo = Player("Bob", Color.BLACK, PlayerType.SMART_BOT)

        root.pokaniService.initializeGame(
            listOf(playerOne, playerTwo), shuffled = false
        )

        val amountMoves = 10

        root.aiService.simulateGame(maxMoves = amountMoves, debug = false)

        val expectation = root.game.copy()

        root.pokaniService.undoMove()
        root.pokaniService.redoMove()

        assertEquals(expectation, root.game)

        repeat(amountMoves) {
            root.pokaniService.undoMove()
        }

        assertEquals(0, root.game.moveID)

        repeat(amountMoves) {
            root.pokaniService.redoMove()
        }

        assertEquals(expectation, root.game)
    }

    @Test
    fun testSaveAndLoadGame() {
        val root = RootService()

        val playerOne = Player("Alice", Color.WHITE, PlayerType.SMART_BOT)
        val playerTwo = Player("Bob", Color.BLACK, PlayerType.SMART_BOT)

        root.pokaniService.initializeGame(
            listOf(playerOne, playerTwo)
        )

        root.aiService.simulateGame(maxMoves = 10, debug = false)

        root.pokaniService.saveGame()

        val expectation = root.game.copy()

        root.pokaniService.loadGame()

        assertEquals(expectation, root.game)
    }
}