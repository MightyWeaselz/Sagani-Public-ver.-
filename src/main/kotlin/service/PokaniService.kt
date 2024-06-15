package service

import MAX_PLAYERS
import OFFER_SIZE
import SAVES_FOLDER
import SAVE_GAME_FILE_NAME
import edu.udo.cs.sopra.ntf.Color
import entity.Player
import entity.PlayerType
import entity.PokaniGame
import entity.move.AbstractMove
import entity.move.AbstractPlaceMove
import entity.move.moves.IntermezzoMove
import entity.move.moves.PlaceMove
import entity.move.moves.SkipMove
import entity.move.moves.WildcardMove
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import java.io.File
import java.io.FileWriter

/**
 * Service layer class that provides the logic for actions to initialize a game, refresh it or
 * redo or undo moves
 *
 * @param root expects the [RootService]
 */
class PokaniService(private val root: RootService) : AbstractRefreshingService() {

    /**
     * [SerializersModule] for [AbstractMove] polymorphism
     */
    private val module = SerializersModule {
        polymorphic(AbstractMove::class) {
            subclass(PlaceMove::class, PlaceMove.serializer())
            subclass(IntermezzoMove::class, IntermezzoMove.serializer())
            subclass(WildcardMove::class, WildcardMove.serializer())
            subclass(SkipMove::class, SkipMove.serializer())
        }
    }

    /**
     * [Json] object for serialization
     */
    private val json = Json {
        allowStructuredMapKeys = true
        serializersModule = module
    }

    /**
     * Initializes a new game (overwriting a currently active one, if it exists)
     * @param players a list of player objects [Player] with a size between 2 and 4
     * @param shuffled whether the draw pile should be shuffled
     */
    fun initializeGame(players: List<Player>, shuffled: Boolean = true) {
        check(players.size in 2..MAX_PLAYERS) {
            throw InvalidPlayerCountException(players.size)
        }

        root.game = PokaniGame(players)

        populateDrawPile(shuffled = shuffled)

        if (root.networkService.isOnlineGame) root.networkService.sendHostGameInit()

        populateDisplay()

        onAllRefreshables { refreshOnStartGame() }
    }

    /**
     * Verifies the player data and returns a [Player] object
     * @param name the name of the player
     * @param color the color of the player
     * @param playerType the player type of the player
     */
    fun verifyPlayerData(name: String, color: Color, playerType: PlayerType): Player {
        val sanitizedName = name.trim()
        check(sanitizedName.isNotBlank()) {
            throw PlayerNameNotValidException(sanitizedName)
        }

        return Player(sanitizedName, color, playerType)
    }

    /**
     * Populates the [drawStack] with all [Tile]s (and [Connection]s) in a random order
     */
    fun populateDrawPile(shuffled: Boolean = true) {
        val originalTiles = TileFactory.getOriginalTiles()

        val stack = if (shuffled) {
            originalTiles.shuffled()
        } else originalTiles

        root.game.drawPile.addAll(stack)
    }

    /**
     * Draws five [Tile]s into display from [drawStack]
     */
    fun populateDisplay() {
        if (root.game.wildCardEnd) {
            repeat(root.game.drawPile.size) {
                root.game.offerDisplay.add(0, root.game.drawPile.removeFirst())
            }
        } else {
            // The order has to be correct
            repeat(OFFER_SIZE) {
                root.game.offerDisplay.add(0, root.game.drawPile.removeFirst())
            }
        }

//        println("Display populated with: ${draws.joinToString(" ")}\n")
    }


    /**
     * Reverts the display to the state before the last move
     */
    fun revertDisplay() {
        repeat(root.game.offerDisplay.size) {
            root.game.drawPile.add(0, root.game.offerDisplay.removeLast())
        }
    }

    /**
     * Executes a move and refresh the grid
     */
    fun executeMove(
        move: AbstractMove, isSimulation: Boolean = false,
        sendMoveOnline: Boolean = !isSimulation, isRedo: Boolean = false
    ) {
        if (!isRedo) root.game.redoStack.clear()
        root.game.undoStack.add(0, move)
        val executeMovePlayer = root.game.currentPlayer

        if (move is AbstractPlaceMove) {
            move.checkAndExecute(root)

            if (root.game.offerDisplay.isEmpty() && !root.game.isIntermezzo) {
                move.populatedTiles = true
                populateDisplay()
            }
        } else {
            move.execute(root)
        }

        onAllRefreshables { refreshPreMove(sendMoveOnline, move, executeMovePlayer, isSimulation) }
    }

    fun executeSendMove(
        sendMoveOnline: Boolean,
        move: AbstractMove,
        executeMovePlayer: Player,
        isSimulation: Boolean
    ) {
        if (root.networkService.isOnlineGame && sendMoveOnline) root.networkService.sendMove(move, executeMovePlayer)

        if (root.game.endConditionsMet) {
            root.game.lastRound = true
        }

        if (checkAndEndGame(isSimulation)) return

        if (isSimulation) return

        onAllRefreshables { refreshOnRedo(executeMovePlayer) }
    }

    /**
     * Redo a move
     */
    fun redoMove() {
        val move = root.game.redoStack.removeFirstOrNull()
            ?: throw AbstractPlaceMove.MoveNotExecutedException()

        executeMove(move, isRedo = true)
    }

    /**
     * Undo a move and refresh the grid
     */
    fun undoMove(isSimulation: Boolean = false) {
        val move = root.game.undoStack.removeFirstOrNull()
            ?: throw AbstractPlaceMove.MoveNotExecutedException()

        if (move is AbstractPlaceMove) {
            if (move.populatedTiles) {
                revertDisplay()
            }
            move.checkAndUndo(root)
        } else {
            move.undo(root)
        }

        if (!root.game.endConditionsMet) {
            root.game.lastRound = false
        }

        root.game.redoStack.add(0, move)

        if (isSimulation) {
            return
        }
        onAllRefreshables { refreshOnUndo() }
    }

    /**
     * Endgame check to look for an EndGame refresh if the conditions are met
     */
    private fun checkAndEndGame(isSimulation: Boolean) =
        if (!root.game.lastRound || root.game.players.first() != root.game.currentPlayer) {
            false
        } else {
            if (isSimulation) {
                true
            } else {
                if (root.networkService.isOnlineGame) root.networkService.endGame()
                onAllRefreshables { refreshOnEndGame() }
                true
            }
        }


    /**
     * Saves the game as a json file.
     */
    fun saveGame() {
        val serialized = json.encodeToString(PokaniGame.serializer(), root.game)

        val saveGame = File(SAVES_FOLDER, SAVE_GAME_FILE_NAME)

        if (!saveGame.parentFile.exists()) {
            saveGame.parentFile.mkdirs()
        }

        val saveGameWriter = FileWriter(saveGame)
        saveGameWriter.write(serialized)
        saveGameWriter.close()
    }

    /**
     * Saves the game as a json file.
     */
    fun loadGame() {
        val saveGame = File(SAVES_FOLDER, SAVE_GAME_FILE_NAME)

        if (!saveGame.exists()) return

        val saveGameReader = saveGame.bufferedReader()
        val serialized = saveGameReader.readText()
        saveGameReader.close()

        val game = json.decodeFromString(PokaniGame.serializer(), serialized)

        root.game = game

        onAllRefreshables { refreshOnStartGame() }

    }

    /**
     * Throws an exception when the number of players is invalid
     */
    class InvalidPlayerCountException(amount: Int) :
        Exception("Wrong amount of players ($amount). A maximum of 2-$MAX_PLAYERS players are allowed")

    /**
     * Throws an exception when the player name is invalid
     */
    class PlayerNameNotValidException(name: String) :
        Exception("Invalid player name: $name. A player name is not allowed to contain blanks or be empty.")
}
