package service

import edu.udo.cs.sopra.ntf.Color
import entity.Player
import entity.PlayerType
import entity.move.AbstractPlaceMove
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AIServiceTest {
    @Test
    fun testRandomMove() {
        val root = RootService()

        root.pokaniService.initializeGame(
            listOf(
                Player("Alice", Color.WHITE, PlayerType.RANDOM_BOT),
                Player("Bob", Color.BLACK, PlayerType.RANDOM_BOT)
            )
        )

        val player = root.game.currentPlayer

        val move = root.aiService.getRandomMove(
            root.game.offerDisplay,
            player,
            root.game.moveID
        )

        root.pokaniService.executeMove(move)

        assertEquals(1, player.grid.tiles.size)
    }

    @Test
    fun simulateSmartVSRandomGame() {
        val root = RootService()

        root.pokaniService.initializeGame(
            listOf(
                Player("Alice", Color.WHITE, PlayerType.SMART_BOT),
                Player("Bob", Color.BLACK, PlayerType.RANDOM_BOT)
            )
        )

        root.aiService.simulateGame()

        assertTrue(root.game.players.first().score > root.game.players.last().score)
    }

    @Test
    fun simulateSingleTwoPlayerGame() {
        val root = RootService()

        root.pokaniService.initializeGame(
            listOf(
                Player("Alice", Color.WHITE, PlayerType.SMART_BOT),
                Player("Bob", Color.BLACK, PlayerType.SMART_BOT)
            )
        )

        root.aiService.simulateGame()
    }

    @Test
    fun simulateSingleThreePlayerGame() {
        val root = RootService()

        root.pokaniService.initializeGame(
            listOf(
                Player("Alice", Color.WHITE, PlayerType.SMART_BOT),
                Player("Bob", Color.BLACK, PlayerType.SMART_BOT),
                Player("Charlie", Color.BROWN, PlayerType.SMART_BOT)
            )
        )

        root.aiService.simulateGame(debug = false)
    }

    @Test
    fun simulateSingleFourPlayerGame() {
        val root = RootService()

        root.pokaniService.initializeGame(
            listOf(
                Player("Alice", Color.WHITE, PlayerType.SMART_BOT),
                Player("Bob", Color.BLACK, PlayerType.SMART_BOT),
                Player("Charlie", Color.BROWN, PlayerType.SMART_BOT),
                Player("Dennis", Color.GREY, PlayerType.SMART_BOT)
            )
        )

        root.aiService.simulateGame(debug = false)
    }

    @Test
    fun validateAIPerformance() {
        val amountGames = 100

        val noFlips = compareByDescending<AbstractPlaceMove> {
            it.satisfactionsPerDisc
        }.thenBy {
            it.distanceToOrigin
        }

        println("\nStrategy: satisfaction / disc then by distance to origin")
        validateComparatorPerformance(amountGames, noFlips)

        val flipsTop = compareByDescending<AbstractPlaceMove> {
            it.paybackValue
        }.thenByDescending {
            it.satisfactionsPerDisc
        }.thenBy {
            it.distanceToOrigin
        }

        println("\nStrategy: paybackValue then by satisfaction / disc then by distance to origin")
        validateComparatorPerformance(amountGames, flipsTop)

        val deficitTop = compareBy<AbstractPlaceMove> {
            it.deficit
        }.thenByDescending {
            it.paybackValue
        }.thenByDescending {
            it.satisfactionsPerDisc
        }.thenBy {
            it.distanceToOrigin
        }

        println("\nStrategy: deficit then by paybackValue then by satisfaction / disc then by distance to origin")
        validateComparatorPerformance(amountGames, deficitTop)

        val flipthendefi = compareByDescending<AbstractPlaceMove> {
            it.paybackValue
        }.thenBy {
            it.deficit
        }.thenByDescending {
            it.satisfactionsPerDisc
        }.thenBy {
            it.distanceToOrigin
        }

        println("\nStrategy: paybackValue then by deficit then by satisfaction / disc then by distance to origin")
        validateComparatorPerformance(amountGames, flipthendefi)

        val deficitTopAnd = compareBy<AbstractPlaceMove> {
            it.deficit
        }.thenByDescending {
            it.paybackValue
        }.thenByDescending {
            it.satisfactionsPerDisc
        }.thenBy {
            it.toPlaceTile.price
        }.thenBy {
            it.distanceToOrigin
        }

        println(
            "\nStrategy: deficit then by paybackValue then by satisfaction /" +
                    " disc then by price then by distance to origin"
        )
        validateComparatorPerformance(amountGames, deficitTopAnd)
    }

    private fun validateComparatorPerformance(amountGames: Int, comparator: Comparator<AbstractPlaceMove>) {
        repeat(3) { playerNumber ->
            val startTime = System.currentTimeMillis()

            val games: MutableList<List<Player>> = mutableListOf()

            repeat(amountGames) { gameNumber ->
                val root = RootService()

                val players = listOf(
                    Player("Alice", Color.WHITE, PlayerType.SMART_BOT),
                    Player("Bob", Color.BLACK, PlayerType.SMART_BOT),
                    Player("Charlie", Color.BROWN, PlayerType.SMART_BOT),
                    Player("Dennis", Color.GREY, PlayerType.SMART_BOT)
                )

                root.pokaniService.initializeGame(
                    players.subList(0, playerNumber + 2)
                )

                games.add(root.aiService.simulateGame(comparator, debug = false))

                if (gameNumber % 500 == 0 && gameNumber != 0) {
                    printSimulationStats(gameNumber, playerNumber + 2, startTime, games)
                }
            }

            printSimulationStats(amountGames, playerNumber + 2, startTime, games)
        }
    }

    private fun printSimulationStats(amountGames: Int, numPlayers: Int, startTime: Long, games: List<List<Player>>) {
        println(
            "Game #$amountGames at speed ${
                "%.2f".format(
                    amountGames / ((System.currentTimeMillis() - startTime) / 1000.0)
                )
            } games/s with $numPlayers players." +
                    "\nAvg bought cacophony discs: (${
                        "%.2f".format(games.flatten().map { it.totalDiscs - 24 }.average())
                    } per game)\nAvg win rate: ${
                        games.groupBy { it.first().name }.map { it.key to it.value.size }
                            .sortedByDescending { it.second }
                            .joinToString(" ") {
                                "${it.first}: ${
                                    "%.2f".format(
                                        it.second.toDouble() / amountGames * 100
                                    )
                                }%"
                            }
                    }"
        )
    }
}