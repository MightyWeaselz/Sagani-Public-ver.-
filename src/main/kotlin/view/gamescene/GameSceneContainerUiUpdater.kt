package view.gamescene

import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.visual.CompoundVisual
import view.PokaniApplication
import view.Textures
import view.customComponent.CustomCornerStonesComponent
import view.customComponent.CustomTileComponent
import view.customComponent.Origin

/**
 * Class for updating tile containers of the UI of the [GameScene].
 */
class GameSceneContainerUiUpdater(private val pokaniApplication: PokaniApplication) {

    val textures = Textures()

    /**
     * Updates the Offer Display to show the current Tiles inside it
     */
    fun updateDisplayLayout() {
        val currentGame = pokaniApplication.rootService.game
        currentGame.offerDisplay.forEach {
            val tile = pokaniApplication.gameScene.tiles[it.tileID - 1]
            if (tile.parent != null) {
                tile.removeFromParent()
            }
            pokaniApplication.gameScene.displayLayout.add(tile)
            tile.showFront()
            tile.origin = Origin.OFFER_DISPLAY
            tile.height = 100.0
            tile.width = 100.0
            resetTile(tile)
        }
    }

    /**
     * Updates the Intermezzo Display to show the current Tiles inside it
     */
    fun updateIntermezzo() {
        val currentGame = pokaniApplication.rootService.game
        pokaniApplication.gameScene.intermezzoLayout.clear()
        currentGame.intermezzoDisplay.forEach {
            val tile = pokaniApplication.gameScene.tiles[it.tileID - 1]
            if (tile.parent != null) {
                tile.removeFromParent()
            }
            pokaniApplication.gameScene.intermezzoLayout.add(tile)
            tile.origin = Origin.INTERMEZZO
            tile.height = 100.0
            tile.width = 100.0
            resetTile(tile)
        }
    }

    /**
     * Updates the Draw Pile Display to show the current Tiles inside it
     */
    fun updateDrawPiles() {
        val drawPile = pokaniApplication.rootService.game.drawPile
        var pileIndex = 2
        var tileCounter = 1
        val reversedDrawPile = drawPile.reversed()
        reversedDrawPile.forEach {
            if (tileCounter > 24) {
                pileIndex--
                tileCounter = 1
            }
            val tile = pokaniApplication.gameScene.tiles[it.tileID - 1]
            if (tile.parent != null) {
                tile.removeFromParent()
            }
            pokaniApplication.gameScene.drawPiles[pileIndex].add(tile)
            tile.origin = Origin.DRAW_PILE
            tile.height = 100.0
            tile.width = 100.0
            resetTile(tile)
            tileCounter++
        }
    }

    /**
     * Updates the grids of all the Players to show the correctly placed Tiles inside of them
     */
    fun updateGrids() {
        pokaniApplication.rootService.game.players.forEachIndexed { index, player ->
            player.grid.tiles.forEach { (position, tile) ->
                val selectedTile = pokaniApplication.gameScene.tiles[tile.tileID - 1]
                if (selectedTile.parent != null) {
                    selectedTile.removeFromParent()
                }
                pokaniApplication.gameScene.playerPanes[index].add(selectedTile)
                selectedTile.posX = (3996 + 100 * position.x).toDouble()
                selectedTile.posY = (2204 + 100 * -position.y).toDouble()
                selectedTile.height = 100.0
                selectedTile.width = 100.0
                selectedTile.rotation = tile.rotation.ordinal * 45.0
                if (tile.isFlipped) {
                    selectedTile.showBack()
                    selectedTile.rotation = 0.0
                } else {
                    selectedTile.showFront()
                    tile.connections.forEachIndexed { index, connection ->
                        if (connection.isSatisfied) {
                            (selectedTile.frontVisual as CompoundVisual).children[index + 1].transparency = 0.0
                        }
                    }
                }
                selectedTile.isDraggable = false
            }
        }
    }

    /**
     * Resets and centers all the [CameraPane]s of all the Players
     */
    fun centerCameras() {
        repeat(pokaniApplication.rootService.game.players.size) { index ->
            val delay = DelayAnimation(50).apply {
                onFinished = {
                    pokaniApplication.gameSceneGetters.getPlayerCam(index).zoom = 1.0
                    pokaniApplication.gameSceneGetters.getPlayerCam(index).panBy(-1000, -1000)
                    pokaniApplication.gameSceneGetters.getPlayerCam(index).pan(3084, 1712)
                }
            }
            pokaniApplication.gameScene.playAnimation(delay)
        }
    }

    /**
     * creates all the [Pane]s and [CameraPane]s of all the Players
     */
    fun createPlayerPanesAndCams() {
        val currentGame = pokaniApplication.rootService.game
        repeat(currentGame.players.size) {
            val pane: Pane<CardView> = Pane(width = 8192, height = 4608)
            pane.add(CustomCornerStonesComponent(0, 0))
            pane.add(CustomCornerStonesComponent(8092, 0))
            pane.add(CustomCornerStonesComponent(0, 4508))
            pane.add(CustomCornerStonesComponent(8092, 4508))
            val cameraPane: CameraPane<Pane<CardView>> = CameraPane(
                width = 1920, height = 1080, target = pane,
                visual = CompoundVisual(
                    textures.activeBorder.apply { transparency = 0.0 }
                )
            ).apply {
                interactive = true
            }
            pokaniApplication.gameScene.playerPanes.add(pane)
            pokaniApplication.gameScene.playerCams.add(cameraPane)
            pokaniApplication.gameScene.addComponents(cameraPane)
        }
    }

    /**
     * Resets the Tile to its default State
     *
     * @param tile the Tile to be reset
     */
    private fun resetTile(tile: CustomTileComponent) {
        tile.rotation = 0.0
        tile.isDisabled = false
    }

    /**
     * Hides the Tile Modify Overlay Buttons
     */
    fun hideTileModifyOverlay() {
        pokaniApplication.gameScene.rotateLeftButton.isDisabled = true
        pokaniApplication.gameScene.rotateLeftButton.isVisible = false
        pokaniApplication.gameScene.rotateRightButton.isDisabled = true
        pokaniApplication.gameScene.rotateRightButton.isVisible = false
        pokaniApplication.gameScene.confirmButton.isDisabled = true
        pokaniApplication.gameScene.confirmButton.isVisible = false
        pokaniApplication.gameScene.cancelButton.isDisabled = true
        pokaniApplication.gameScene.cancelButton.isVisible = false
    }
}