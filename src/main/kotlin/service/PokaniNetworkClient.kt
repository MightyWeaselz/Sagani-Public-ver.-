package service

import edu.udo.cs.sopra.ntf.ConnectionState
import edu.udo.cs.sopra.ntf.GameInitMessage
import edu.udo.cs.sopra.ntf.TurnMessage
import entity.PlayerType
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.net.client.BoardGameClient
import tools.aqua.bgw.net.client.NetworkLogging
import tools.aqua.bgw.net.common.annotations.GameActionReceiver
import tools.aqua.bgw.net.common.notification.PlayerJoinedNotification
import tools.aqua.bgw.net.common.notification.PlayerLeftNotification
import tools.aqua.bgw.net.common.response.CreateGameResponse
import tools.aqua.bgw.net.common.response.CreateGameResponseStatus
import tools.aqua.bgw.net.common.response.JoinGameResponse
import tools.aqua.bgw.net.common.response.JoinGameResponseStatus

/**
 * Class that handles the messages sent between the Client and the Server when playing an Online Game
 */
class PokaniNetworkClient(
    playerName: String,
    host: String,
    secret: String,
    var networkService: NetworkService,
    var playerType: PlayerType
) : BoardGameClient(playerName, host, secret, NetworkLogging.VERBOSE) {

    /**
     * Handle a [CreateGameResponse] sent by the server. Will await the guest player when its
     * status is [CreateGameResponseStatus.SUCCESS]. As recovery from network problems is not
     * implemented in Sagni, the method disconnects from the server and throws an
     * [IllegalStateException] otherwise.
     *
     * @throws IllegalStateException if status != success or currently not waiting for a game creation response.
     */
    override fun onCreateGameResponse(response: CreateGameResponse) {
        check(networkService.connectionState == ConnectionState.WAITING_FOR_HOST_CONFIRMATION)
        { "unexpected CreateGameResponse" }

        if (response.status == CreateGameResponseStatus.SUCCESS) {
            networkService.updateConnectionState(ConnectionState.WAITING_FOR_GUESTS)
        } else {
            disconnectAndError(response.status.name)
        }
    }

    /**
     * Handle a [JoinGameResponse] sent by the server. Will await the init message when its
     * status is [JoinGameResponseStatus.SUCCESS]. As recovery from network problems is not
     * implemented in Sagani, the method disconnects from the server and throws an
     * [IllegalStateException] otherwise.
     *
     * @throws IllegalStateException if status != success or currently not waiting for a join game response.
     */
    override fun onJoinGameResponse(response: JoinGameResponse) {
        check(networkService.connectionState == ConnectionState.WAITING_FOR_JOIN_CONFIRMATION)
        { "unexpected JoinGameResponse" }

        if (response.status == JoinGameResponseStatus.SUCCESS) {
            networkService.updateConnectionState(ConnectionState.WAITING_FOR_INIT)
        } else {
            disconnectAndError(response.status.name)
        }

        BoardGameApplication.runOnGUIThread {
            networkService.handleJoinedGame(response)
        }
    }

    /**
     * handle a [GameInitMessage] sent by the server
     *
     * @param message [GameInitMessage] from BGW net
     * @param sender sender from BGW net
     */
    @Suppress("UNUSED_PARAMETER", "unused")
    @GameActionReceiver
    fun onGameInitReceived(message: GameInitMessage, sender: String) {
        check(networkService.connectionState == ConnectionState.WAITING_FOR_INIT)
        { "unexpected gameInitMessage" }
        BoardGameApplication.runOnGUIThread {
            networkService.startNewJoinedGame(
                message
            )
        }
    }

    /**
     * handle a [PlayerJoinedNotification] sent by the server
     *
     * @param notification [PlayerJoinedNotification] from BGW net
     */
    override fun onPlayerJoined(notification: PlayerJoinedNotification) {
        BoardGameApplication.runOnGUIThread {
            networkService.newPlayerJoined(notification)
        }
    }

    /**
     * handle a [PlayerLeftNotification] sent by the server
     *
     * @param notification [PlayerLeftNotification] from BGW net
     */
    override fun onPlayerLeft(notification: PlayerLeftNotification) {
        BoardGameApplication.runOnGUIThread {
            networkService.playerLeft(notification)
        }
    }

    /**
     * handle a [TurnMessage] sent by the server
     *
     * @param message [TurnMessage] from BGW net
     * @param sender sender of message from BGW net
     */
    @Suppress("UNUSED_PARAMETER", "unused")
    @GameActionReceiver
    fun onTurnMessageReceived(message: TurnMessage, sender: String) {
        check(networkService.connectionState == ConnectionState.WAITING_FOR_OPPONENTS)
        { "unexpected turnMessage" }
        BoardGameApplication.runOnGUIThread {
            networkService.applyReceivedMove(message)
        }
    }

    /**
     * Disconnect on error
     *
     * @param message message to error on
     */
    private fun disconnectAndError(message: String) {
        networkService.disconnect()
        networkService.updateConnectionState(ConnectionState.DISCONNECTED)
        throw IllegalStateException(message)
    }

}