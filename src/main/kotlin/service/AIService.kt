package service

import INTERMEZZO_SIZE
import entity.Player
import entity.PlayerType
import entity.PokaniGame
import entity.components.BaseTile
import entity.components.Position
import entity.components.Rotation
import entity.move.AbstractPlaceMove
import entity.move.moves.IntermezzoMove
import entity.move.moves.PlaceMove
import entity.move.moves.WildcardMove

/**
 * The [AIService] is responsible for calculating the optimal move for a [PokaniGame].
 * @property root the [RootService] of the [AIService]
 */
class AIService(private val root: RootService) {
    // Improvements:
    // -> take the best tile of player with the highest score that is still fitting for self
    // -> higher satisfaction per coin than the opponent would lose as opportunity
    //    always try to make the outcome profitable for self (others loose satis per coin and self-gains)
    // -> take into account that the opponent might be able to place a tile that satisfies more connections
    // -> plan ahead for the next round best placement
    // -> plan ahead to create more opportunities for self
    // -> take into account the last round of the game

    /**
     * Orders the possible moves so the first move is the best move.
     */
    private val defaultMoveComparator: Comparator<AbstractPlaceMove>
        get() = compareBy<AbstractPlaceMove> {
            it.deficit
        }.thenByDescending {
            it.paybackValue
        }.thenByDescending {
            it.satisfactionsPerDisc
        }.thenBy {
            it.distanceToOrigin
        }

    /**
     * Calculates the optimal move.
     */
    fun calculateOptimalMove(
        baseTiles: List<BaseTile>,
        player: Player,
        moveID: Int,
        moveOrder: Comparator<AbstractPlaceMove> = defaultMoveComparator,
        simulationSteps: Int = 1,
        allowSkip: Boolean = true,
        stealTiles: Boolean = true
    ) = baseTiles.toList().flatMap { baseTile ->
        root.gridService.getValidPositions(player.grid).flatMap { position ->
            Rotation.orthogonals().map { rotation ->
                val move = getMoveBasedOnTile(position, rotation, baseTile, moveID, player)

                root.pokaniService.executeMove(move, true)
                root.pokaniService.undoMove(true)

                move
            }
        }
    }.sortedWith(moveOrder).firstOrNull() ?: throw IllegalStateException("No valid move found.")

    /**
     * Simulates a game.
     */
    fun simulateGame(
        comparator: Comparator<AbstractPlaceMove> = defaultMoveComparator, simulationSteps: Int = 1,
        allowSkip: Boolean = true, stealTiles: Boolean = true, maxMoves: Int = Int.MAX_VALUE, debug: Boolean = true
    ): List<Player> {
        val startTime = System.currentTimeMillis()

        while (!root.game.endConditionsMet && root.game.undoStack.size < maxMoves) {
            if (debug) {
                println("\n--------------------Move (#${root.game.moveID})--------------------\n")
                println(
                    root.game.currentPlayer.grid.toString(
                        root.gridService.getValidPositions(root.game.currentPlayer.grid),
                        root.game.currentPlayer
                    )
                )
            }

            val currentSelection = when {
                root.game.isIntermezzo -> root.game.intermezzoDisplay
                root.game.offerDisplay.size > 1 -> root.game.offerDisplay
                else -> listOf(root.game.drawPile.first()) + root.game.offerDisplay
            }

            if (debug) {
                println("Draw Pile: ${root.game.drawPile}\n")
                println("Offer Display: ${root.game.offerDisplay}\n")
                println("Actual: ${currentSelection}\n")
            }

            if (debug && root.game.intermezzoDisplay.isNotEmpty()) {
                val tileList = root.game.intermezzoDisplay.joinToString(" ") {
                    "${root.game.intermezzoDisplay.indexOf(it) + 1}: $it"
                }

                println("Intermezzo (${root.game.intermezzoDisplay.size}/$INTERMEZZO_SIZE): $tileList\n")
            }

            val move = if (root.game.currentPlayer.playerType == PlayerType.SMART_BOT) {
                root.aiService.calculateOptimalMove(
                    currentSelection,
                    root.game.currentPlayer,
                    root.game.moveID,
                    comparator,
                    simulationSteps,
                    allowSkip,
                    stealTiles
                )
            } else {
                root.aiService.getRandomMove(
                    currentSelection,
                    root.game.currentPlayer,
                    root.game.moveID
                )
            }

            val lastPlayer = root.game.currentPlayer

            root.pokaniService.executeMove(move)

            if (debug) {
                println(move.toString())
                println(
                    lastPlayer.grid.toString(
                        root.gridService.getValidPositions(lastPlayer.grid),
                        lastPlayer
                    )
                )
            }
        }

        val sortedPlayers = root.game.players.sortedByDescending { it.score }

        if (debug) sortedPlayers.joinToString("") {
            "${sortedPlayers.indexOf(it) + 1}: $it \n"
        }.let {
            println("Scores: \n$it")
        }

        if (debug) println(
            "Game ended after ${root.game.moveID + 1} moves. " +
                    "Simulation took ${System.currentTimeMillis() - startTime}ms."
        )

        return sortedPlayers
    }

    /**
     * @return a random [Move] for the given [Player].
     */
    fun getRandomMove(
        baseTiles: List<BaseTile>,
        player: Player,
        moveID: Int
    ): AbstractPlaceMove {
        val randomBaseTile = baseTiles.random()
        val randomPosition = root.gridService.getValidPositions(player.grid).random()
        val randomRotation = Rotation.orthogonals().random()

        return getMoveBasedOnTile(randomPosition, randomRotation, randomBaseTile, moveID, player)
    }

    private fun getMoveBasedOnTile(
        position: Position, rotation: Rotation,
        baseTile: BaseTile, moveID: Int, player: Player
    ) =
        when (baseTile) {
            in root.game.offerDisplay -> {
                PlaceMove(position, rotation, baseTile, moveID, player)
            }

            in root.game.intermezzoDisplay -> {
                IntermezzoMove(position, rotation, baseTile, moveID, player)
            }

            in root.game.drawPile -> {
                WildcardMove(position, rotation, baseTile, moveID, player)
            }

            else -> throw IllegalStateException("No valid move possible.")
        }

    /**
     * Executes a bot move specified by the PlayerType.
     */
    fun executeBotMove(botType: PlayerType) {
        val currentGame = root.game
        val currentPlayer = currentGame.currentPlayer
        val currentSelection = when {
            root.game.isIntermezzo -> root.game.intermezzoDisplay
            root.game.offerDisplay.size > 1 -> root.game.offerDisplay
            else -> listOf(root.game.drawPile.first()) + root.game.offerDisplay
        }
        val randomMove = root.aiService.getRandomMove(
            currentSelection,
            currentPlayer,
            currentGame.moveID,
        )
        val bestMoveCalc = root.aiService.calculateOptimalMove(
            currentSelection,
            currentPlayer,
            currentGame.moveID,
        )
        if (botType == PlayerType.SMART_BOT) {
            root.pokaniService.executeMove(bestMoveCalc)
            return
        }
        root.pokaniService.executeMove(randomMove)
    }
}