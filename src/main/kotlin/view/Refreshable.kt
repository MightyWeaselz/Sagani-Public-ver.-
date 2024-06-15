package view

import entity.Player
import entity.move.AbstractMove

/**
 * the Refreshable Class holds all the Default implementations of the refreshes.
 */
interface Refreshable {
    fun refreshOnStartGame() {}
    fun refreshOnPlaceTile() {}
    fun refreshOnEndGame() {}
    fun refreshOnUndo() {}
    fun refreshOnRedo(executeMovePlayer: Player) {}
    fun refreshOnOnlinePlayerJoin(s: String) {}
    fun refreshOnOnlinePlayerLeft(s: String) {}
    fun refreshOnError() {}
    fun refreshPreMove(sendMoveOnline: Boolean,
                       move: AbstractMove,
                       executeMovePlayer: Player,
                       isSimulation: Boolean) {}
}