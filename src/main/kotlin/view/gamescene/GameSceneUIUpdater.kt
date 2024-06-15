package view.gamescene

import entity.PlayerType
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import view.PokaniApplication
import kotlin.math.ceil

/**
 * Class for updating the UI of the [GameScene].
 */
class GameSceneUIUpdater(private val pokaniApplication: PokaniApplication) {
    /**
     * Update Current Player Labels and set the visual of the camera Pane
     */
    fun updateCurrentLabelsAndCams() {
        val currentGame = pokaniApplication.rootService.game
        pokaniApplication.gameScene.currentPlayerLabel.text = "Current Player: ${currentGame.currentPlayer.name}"
        pokaniApplication.gameScene.currentRoundLabel.text =
            "Round: ${ceil((currentGame.moveID / currentGame.players.size).toDouble()).toInt() + 1}" +
                    "  (${currentGame.players.indexOf(currentGame.currentPlayer) + 1} of ${currentGame.players.size})"
        val cam = pokaniApplication.gameSceneGetters.getPlayerCam(
            pokaniApplication.gameSceneGetters.getCurrentPlayerID()
        )

        if (!pokaniApplication.gameScene.isOnlineGame) {
            return
        }

        if (pokaniApplication.rootService.game.currentPlayer.playerType == PlayerType.REMOTE_PLAYER) {
            return
        }

        val delayShow = DelayAnimation(200).apply {
            onFinished = {
                (cam.visual as CompoundVisual).children[0].transparency = 1.0
            }
        }
        val delayHide = DelayAnimation(200).apply {
            onFinished = {
                (cam.visual as CompoundVisual).children[0].transparency = 0.0
            }
        }
        val sequentialAnimation = SequentialAnimation(
            delayShow,
            delayHide,
            delayShow,
            delayHide,
            delayShow,
            delayHide
        )

        pokaniApplication.gameScene.playAnimation(sequentialAnimation)
    }

    /**
     * Disable Tile Selection
     */
    fun disableSelections() {
        pokaniApplication.gameScene.intermezzoLayout.forEach { it.isDraggable = false }
        pokaniApplication.gameScene.displayLayout.forEach { it.isDraggable = false }
        pokaniApplication.gameScene.drawPiles.forEach { drawPile -> drawPile.forEach { it.isDraggable = false } }
        pokaniApplication.gameScene.tiles.forEach { it.isDraggable = false }
        (pokaniApplication.gameScene.offerDisplayBorder.visual as CompoundVisual).children[0].transparency = 0.0
        (pokaniApplication.gameScene.intermezzoBorder.visual as CompoundVisual).children[0].transparency = 0.0
        (pokaniApplication.gameScene.drawPileBorder.visual as CompoundVisual).children[0].transparency = 0.0
    }

    /**
     * Enable Tile Selection on allowed Selection
     */
    fun enableSelection() {
        val currentGame = pokaniApplication.rootService.game
        if (currentGame.currentPlayer.playerType != PlayerType.LOCAL_PLAYER) {
            return
        }
        when (currentGame.offerDisplay.size) {
            0 -> {
                pokaniApplication.gameScene.intermezzoLayout.forEach { it.isDraggable = true }
                (pokaniApplication.gameScene.intermezzoBorder.visual as CompoundVisual).children[0].transparency = 1.0
            }

            1 -> {
                pokaniApplication.gameScene.displayLayout.forEach { it.isDraggable = true }
                pokaniApplication.gameSceneGetters.getActiveDrawPile().peek().isDraggable = true
                (pokaniApplication.gameScene.offerDisplayBorder.visual as CompoundVisual).children[0].transparency = 1.0
                (pokaniApplication.gameScene.drawPileBorder.visual as CompoundVisual).children[0].transparency = 1.0
            }

            else -> pokaniApplication.gameScene.displayLayout.forEach {
                it.isDraggable = true
                (pokaniApplication.gameScene.offerDisplayBorder.visual as CompoundVisual).children[0].transparency = 1.0
            }
        }
    }

    /**
     * Pushes all static UI elements to the front
     */
    private fun pushStaticUIToFront() {
        pokaniApplication.gameScene.offerDisplayBorder.toFront()
        pokaniApplication.gameScene.intermezzoBorder.toFront()
        pokaniApplication.gameScene.drawPileBorder.toFront()
        pokaniApplication.gameScene.displayLayout.toFront()
        pokaniApplication.gameScene.intermezzoLayout.toFront()
        pokaniApplication.gameScene.drawPiles.forEach { it.toFront() }
        pokaniApplication.gameScene.currentInformationGrid.toFront()
        pokaniApplication.gameScene.centerButton.toFront()
        pokaniApplication.gameScene.undoButton.toFront()
        pokaniApplication.gameScene.redoButton.toFront()
        pokaniApplication.gameScene.skipButton.toFront()
        pokaniApplication.gameScene.spectatingLabel.toFront()
        pokaniApplication.gameScene.errorLabel.toFront()
    }

    /**
     * Shows the Player Cam
     */
    private fun showCurrentPlayerCam() {
        showPlayerCam(pokaniApplication.gameSceneGetters.getCurrentPlayerID())
    }

    /**
     * Shows the Camera defined by the playerID
     *
     * @param playerID the ID of the Player of which to show the [CameraPane]
     */
    fun showPlayerCam(playerID: Int) {
        pokaniApplication.gameScene.playerCams.filterIndexed { index, _ ->
            index != playerID
        }.forEach {
            it.isVisible = false
        }
        pokaniApplication.gameScene.playerCams[playerID].isVisible = true

        pokaniApplication.gameScene.playerClusters.filterIndexed { index, _ -> index != playerID }.forEach {
            (it[0].visual as CompoundVisual).children[0].transparency = 0.0
            it[1].visual = ColorVisual(150, 150, 150, 0)
            it[2].visual = ColorVisual(150, 150, 150, 0)
            (it[3].visual as CompoundVisual).children[0].transparency = 0.0
            it[4].visual = ColorVisual(150, 150, 150, 0)
        }

        val currentCluster = pokaniApplication.gameScene.playerClusters[playerID]
        (currentCluster[0].visual as CompoundVisual).children[0].transparency = 1.0
        currentCluster[1].visual = ColorVisual(150, 150, 150)
        currentCluster[2].visual = ColorVisual(150, 150, 150)
        (currentCluster[3].visual as CompoundVisual).children[0].transparency = 1.0
        currentCluster[4].visual = ColorVisual(150, 150, 150)
    }

    /**
     * Update Tile Modify Overlay on Current Player Pane
     */
    private fun updateTileModifyOverlay() {
        val gameScene = pokaniApplication.gameScene
        val gameSceneGetters = pokaniApplication.gameSceneGetters

        //remove Tile modify Overlay
        if (gameScene.rotateLeftButton.parent != null) {
            gameScene.rotateLeftButton.removeFromParent()
        }
        if (gameScene.rotateRightButton.parent != null) {
            gameScene.rotateRightButton.removeFromParent()
        }
        if (gameScene.confirmButton.parent != null) {
            gameScene.confirmButton.removeFromParent()
        }
        if (gameScene.cancelButton.parent != null) {
            gameScene.cancelButton.removeFromParent()
        }
        // Add Tile Modify Overlay for current Player
        if (gameScene.rotateLeftButton.parent == null) {
            gameScene.playerPanes[gameSceneGetters.getCurrentPlayerID()].add(gameScene.rotateLeftButton)
        }
        if (gameScene.rotateRightButton.parent == null) {
            gameScene.playerPanes[gameSceneGetters.getCurrentPlayerID()].add(gameScene.rotateRightButton)
        }
        if (gameScene.confirmButton.parent == null) {
            gameScene.playerPanes[gameSceneGetters.getCurrentPlayerID()].add(gameScene.confirmButton)
        }
        if (gameScene.cancelButton.parent == null) {
            gameScene.playerPanes[gameSceneGetters.getCurrentPlayerID()].add(gameScene.cancelButton)
        }
        pokaniApplication.gameSceneContainerUiUpdater.hideTileModifyOverlay()
    }

    /**
     * Toggles the Skip Button Visibility
     */
    private fun toggleSkipButtonVisibility() {
        val skipVis = pokaniApplication.rootService.game.isIntermezzo
        if (skipVis && pokaniApplication.rootService.game.currentPlayer.playerType == PlayerType.LOCAL_PLAYER) {
            pokaniApplication.gameScene.skipButton.isVisible = true
            pokaniApplication.gameScene.skipButton.isDisabled = false
        } else {
            pokaniApplication.gameScene.skipButton.isVisible = false
            pokaniApplication.gameScene.skipButton.isDisabled = true
        }
    }

    /**
     * Updates the Player Info Clusters
     */
    private fun updatePlayerInfoClusters() {
        val currentGame = pokaniApplication.rootService.game
        currentGame.players.forEachIndexed { index, player ->
            val playerCluster = pokaniApplication.gameScene.playerClusters[index]
            playerCluster[1].text = player.score.toString()
            playerCluster[2].text = "${player.currentDiscs} / ${player.totalDiscs}"
        }
    }

    /**
     * function to manage the undo and redo button to be shown only when
     */
    private fun checkUndoRedoStackVisibility() {
        if (pokaniApplication.gameScene.isOnlineGame) {
            return
        }
        pokaniApplication.gameScene.undoButton.isVisible = pokaniApplication.rootService.game.undoStack.isNotEmpty()
        pokaniApplication.gameScene.undoButton.isDisabled = pokaniApplication.rootService.game.undoStack.isEmpty()
        pokaniApplication.gameScene.redoButton.isVisible = pokaniApplication.rootService.game.redoStack.isNotEmpty()
        pokaniApplication.gameScene.redoButton.isDisabled = pokaniApplication.rootService.game.redoStack.isEmpty()
    }

    /**
     * updates all the UI Components to show the currently correct Information
     */
    fun updateUiAndContainers() {
        updateCurrentLabelsAndCams()
        pokaniApplication.gameSceneContainerUiUpdater.updateGrids()
        pokaniApplication.gameSceneContainerUiUpdater.updateDisplayLayout()
        pokaniApplication.gameSceneContainerUiUpdater.updateIntermezzo()
        pokaniApplication.gameSceneContainerUiUpdater.updateDrawPiles()
        disableSelections()
        enableSelection()
        pushStaticUIToFront()
        showCurrentPlayerCam()
        updateTileModifyOverlay()
        toggleSkipButtonVisibility()
        updatePlayerInfoClusters()
        checkUndoRedoStackVisibility()
        pokaniApplication.gameSceneContainerUiUpdater.centerCameras()
        pokaniApplication.gameScene.hidePossibleSatisfactions()
    }
}