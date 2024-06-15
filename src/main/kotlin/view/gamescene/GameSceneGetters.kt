package view.gamescene

import entity.Player
import entity.components.BaseTile
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import view.PokaniApplication
import view.customComponent.CustomTileComponent

/**
 * Class containing all getters for the [GameScene].
 */
class GameSceneGetters(private val pokaniApplication: PokaniApplication) {
    /**
     * Gets the currently active Draw Pile
     *
     * @return the CardStack of the currently active Draw Pile
     */
    fun getActiveDrawPile(): CardStack<CardView> {
        return pokaniApplication.gameScene.drawPiles.firstOrNull { it.isNotEmpty() } ?: CardStack()
    }

    /**
     * Gets the Current Player
     *
     * @return the Player whose turn it currently is
     */
    private fun getCurrentPlayer(): Player {
        return pokaniApplication.rootService.game.currentPlayer
    }

    /**
     * Gets Current PlayerID
     *
     * @return the ID of the currently active player
     */
    fun getCurrentPlayerID(): Int {
        return pokaniApplication.rootService.game.players.indexOf(getCurrentPlayer())
    }

    /**
     * Gets the currently active Players [Pane]
     *
     * @return the Pane in which the Tiles lay of the current Player
     */
    fun getCurrentPlayerPane(): Pane<CardView> {
        return pokaniApplication.gameScene.playerPanes[getCurrentPlayerID()]
    }

    /**
     * Gets the currently active Players [CameraPane]
     *
     * @return the [CameraPane]
     */
    fun getPlayerCam(playerID: Int): CameraPane<Pane<CardView>> {
        return pokaniApplication.gameScene.playerCams[playerID]
    }

    /**
     * Searches for a Tile in all Displays
     *
     * @param tileToFind the Tile to be found in the entity Layer
     * @return [BaseTile] the Tile of the entity Layer connected to the Visual
     */
    fun getTileInOptions(tileToFind: CustomTileComponent): BaseTile? {
        val currentGame = pokaniApplication.rootService.game
        var baseTile: BaseTile?
        baseTile = currentGame.offerDisplay.find {
            it.tileID == tileToFind.tileID
        }
        if (baseTile == null) {
            baseTile = currentGame.intermezzoDisplay.find {
                it.tileID == tileToFind.tileID
            }
        }
        if (baseTile == null) {
            baseTile = currentGame.drawPile.find {
                it.tileID == tileToFind.tileID
            }
        }
        return baseTile
    }
}