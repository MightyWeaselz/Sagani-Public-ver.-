package service

import edu.udo.cs.sopra.ntf.ConnectionState
import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.Player
import edu.udo.cs.sopra.ntf.TurnMessage
import entity.PlayerType
import entity.PokaniGame
import entity.move.AbstractMove
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.JoinGameResponse

/**
 * Service layer class that realizes the necessary logic for sending and receiving messages
 * in multiplayer network games.
 */
class NetworkService(private val rootService: RootService) : AbstractRefreshingService() {

    companion object {
        /** URL of the BGW net server hosted for SoPra participants */
        const val SERVER_ADDRESS = "sopra.cs.tu-dortmund.de:80/bgw-net/connect"

        /** Name of the game as registered with the server */
        const val GAME_ID = "Sagani"

        /** Secret to connect to server */
        private const val NETWORK_SECRET = "23_b_tbd"
    }

    /** Network client. Nullable for offline games. */
    var client: PokaniNetworkClient? = null
        private set

    /**
     * current state of the connection in a network game.
     */
    var connectionState: ConnectionState = ConnectionState.DISCONNECTED
        private set

    val isOnlineGame: Boolean
        get() = client != null

    /**
     * Connects to server and creates a new game session.
     *
     * @param name Player name.
     * @param sessionID identifier of the hosted session (to be used by guest on join)
     *
     * @throws IllegalStateException if already connected to another game or connection attempt fails
     */
    fun hostGame(name: String, sessionID: String?, playerType: PlayerType) {
        if (!connect(NETWORK_SECRET, name, playerType)) {
            error("Connection failed")
        }
        updateConnectionState(ConnectionState.CONNECTED)

        if (sessionID.isNullOrBlank()) {
            client?.createGame(GAME_ID, "Sagani")
        } else {
            client?.createGame(GAME_ID, sessionID, "Sagani")
        }
        updateConnectionState(ConnectionState.WAITING_FOR_HOST_CONFIRMATION)
    }

    /**
     * Disconnects the [client] from the server, nulls it and updates the
     * [connectionState] to [ConnectionState.DISCONNECTED]. Can safely be called
     * even if no connection is currently active.
     */
    fun disconnect() {
        client?.apply {
            if (connectionState != ConnectionState.DISCONNECTED && connectionState != ConnectionState.CONNECTED)
                leaveGame("Sagani")
            if (isOpen) disconnect()
        }
        client = null
        updateConnectionState(ConnectionState.DISCONNECTED)
    }

    /**
     * Connects to server and joins a game session as guest player.
     *
     * @param name Player name.
     * @param sessionID identifier of the joined session (as defined by host on create)
     *
     * @throws IllegalStateException if already connected to another game or connection attempt fails
     */
    fun joinGame(name: String, sessionID: String, playerType: PlayerType) {
        if (!connect(NETWORK_SECRET, name, playerType)) {
            error("Connection failed")
        }
        updateConnectionState(ConnectionState.CONNECTED)

        client?.joinGame(sessionID, "Sagani")

        updateConnectionState(ConnectionState.WAITING_FOR_JOIN_CONFIRMATION)
    }

    /**
     * Connects to server, sets the [NetworkService.client] if successful and returns `true` on success.
     *
     * @param secret Network secret. Must not be blank (i.e. empty or only whitespaces)
     * @param name Player name. Must not be blank
     *
     * @throws IllegalArgumentException if secret or name is blank
     * @throws IllegalStateException if already connected to another game
     */
    private fun connect(secret: String, name: String, playerType: PlayerType): Boolean {
        require(connectionState == ConnectionState.DISCONNECTED && client == null)
        { "already connected to another game" }

        require(secret.isNotBlank()) { "server secret must be given" }
        require(name.isNotBlank()) { "player name must be given" }

        val newClient =
            PokaniNetworkClient(
                playerName = name,
                host = SERVER_ADDRESS,
                secret = NETWORK_SECRET,
                networkService = this,
                playerType = playerType
            )

        return if (newClient.connect()) {
            this.client = newClient
            true
        } else {
            false
        }
    }

    /**
     * Send move we made
     */
    fun sendMove(move: AbstractMove, player: entity.Player) {
        if (connectionState != ConnectionState.PLAYING_MY_TURN) {
            onAllRefreshables { refreshOnError() }
        } else {
            client?.sendGameActionMessage(move.createNetworkMessage(rootService.game, player))
            updateConnectionState(ConnectionState.WAITING_FOR_OPPONENTS)
        }
    }

    /**
     * Send game wie initiated
     */
    fun sendHostGameInit() {
        require(connectionState == ConnectionState.WAITING_FOR_GUESTS)
        { "can only send host game init when waiting for host guests" }
        client?.sendGameActionMessage(GameInitMessage(
            rootService.game.players.map { Player(it.name, it.color) },
            rootService.game.drawPile.map { it.tileID }
        ))

        if ((rootService.game.players.firstOrNull()
                ?: throw IllegalStateException("")).playerType != PlayerType.REMOTE_PLAYER
        ) {
            updateConnectionState(ConnectionState.PLAYING_MY_TURN)
        } else {
            updateConnectionState(ConnectionState.WAITING_FOR_OPPONENTS)
        }

        println(rootService.game.players)
    }

    fun handleJoinedGame(message: JoinGameResponse) {
        message.opponents.forEach {
            onAllRefreshables { refreshOnOnlinePlayerJoin(it) }
        }
    }

    /**
     * Configure our game the way somebody else started
     */
    fun startNewJoinedGame(message: GameInitMessage) {
        val game = PokaniGame(message.players.map {
            it.mapToLocalPlayer(
                client ?: throw IllegalStateException("Client is not existent, when starting game")
            )
        })
        val originalTiles = TileFactory.getOriginalTiles()
        game.drawPile.addAll(originalTiles.sortedBy { ourCard ->
            message.drawPile.indexOf(ourCard.tileID)
        })
        rootService.game = game

        rootService.pokaniService.populateDisplay()

        if ((message.players.firstOrNull()
                ?: throw IllegalArgumentException("Empty playerlist")).name == (client?.playerName
                ?: throw IllegalStateException("Client is non existent"))
        ) {
            updateConnectionState(ConnectionState.PLAYING_MY_TURN)
        } else {
            updateConnectionState(ConnectionState.WAITING_FOR_OPPONENTS)
        }
        println(rootService.game.players)

        onAllRefreshables { refreshOnStartGame() }
    }

    /**
     * Apply move from somebody else
     */
    fun applyReceivedMove(message: TurnMessage) {
        val movePlayer = rootService.game.currentPlayer

        rootService.pokaniService.executeMove(
            message.mapToMove(
                rootService.game
            ), sendMoveOnline = false
        )
        // Verify checksum

        check(message.checksum.score == movePlayer.score)
        { "Score is not matching ${message.checksum.score} <-> ${rootService.game.currentPlayer.score}" }
        check(message.checksum.availableDiscs == movePlayer.currentDiscs)
        {
            "Discs are not matching ${message.checksum.availableDiscs} <-> ${rootService.game.currentPlayer.currentDiscs}"
        }

        if ((rootService.game.currentPlayer.name) == (client?.playerName
                ?: throw IllegalStateException("No client on TurnMessage"))
        ) {
            updateConnectionState(ConnectionState.PLAYING_MY_TURN)
        }
    }

    fun newPlayerJoined(message: PlayerJoinedNotification) {
        onAllRefreshables { refreshOnOnlinePlayerJoin(message.sender) }
    }

    fun playerLeft(message: PlayerLeftNotification) {
        onAllRefreshables { refreshOnOnlinePlayerLeft(message.sender) }
    }

    /**
     * To be called when we calculate that the
     * game is over
     */
    fun endGame() {
        updateConnectionState(ConnectionState.GAME_FINISHED)
    }

    /**
     * Updates the [connectionState] to [newState]
     *
     * This method should always be called from NetworkService
     * with the exception of messages that do not call methods in
     * NetworkService. In that case it is ok to call this method in
     * SaganiNetworkClient
     */
    fun updateConnectionState(newState: ConnectionState) {
        this.connectionState = newState
    }

}