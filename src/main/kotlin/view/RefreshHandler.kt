package view

import entity.Player
import entity.PlayerType
import entity.move.AbstractMove
import entity.move.moves.SkipMove
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.event.KeyCode
import view.gamescene.GameScene
import view.gamescene.GameScenePauseMenu

/**
 * Class handling all refreshes given to the view layer.
 */
class RefreshHandler(private val pokaniApplication: PokaniApplication) : Refreshable {

    /**
     * function which allows the change of the main [ScoreboardScene] to the [GameScene] with the tab Button
     * as well as the [GameScenePauseMenu] with the Esc Button
     */
    private fun addShowGameSceneEvent() {
        pokaniApplication.scoreboardScene.onKeyPressed = {
            if (it.keyCode == KeyCode.TAB) {
                pokaniApplication.showGameScene(pokaniApplication.gameScene)
                pokaniApplication.gameSceneContainerUiUpdater.centerCameras()
            }
            if (it.keyCode == KeyCode.ESCAPE) {
                pokaniApplication.showMenuScene(pokaniApplication.gameScenePauseMenu)
            }
        }
    }

    /**
     * function which allows the change of the main [GameScene] to the [ScoreboardScene] with the tab Button
     * as well as the [GameScenePauseMenu] with the Esc Button
     */
    private fun addShowScoreboardSceneEvent() {
        pokaniApplication.gameScene.onKeyPressed = {
            if (it.keyCode == KeyCode.TAB) {
                pokaniApplication.showGameScene(pokaniApplication.scoreboardScene)
            }
            if (it.keyCode == KeyCode.ESCAPE) {
                pokaniApplication.showMenuScene(pokaniApplication.gameScenePauseMenu)
            }
        }
    }

    /**
     * function which handles the Inputs of the Buttons of the [GameScenePauseMenu]
     */
    private fun addShowGameScenePauseMenuEvent() {
        pokaniApplication.gameScenePauseMenu.apply {
            continueButton.onMouseClicked = {
                pokaniApplication.hideMenuScene()
                saveDialog.isVisible = false
            }
            mainMenuButton.onMouseClicked = {
                pokaniApplication.hideMenuScene()
                pokaniApplication.showMenuScene(pokaniApplication.gameTypeSelectMenu)
                saveDialog.isVisible = false
                pokaniApplication.gameSceneAnimator.stopAnimation()
                if (pokaniApplication.gameScene.isOnlineGame) {
                    pokaniApplication.rootService.networkService.disconnect()
                }
                pokaniApplication.onlineHostLobbyMenu.apply {
                    listOfInputs.forEach {
                        it.text = ""
                    }
                }
                pokaniApplication.onlineJoinLobbyMenu.apply {
                    listOfInputs.forEach {
                        it.text = ""
                    }
                }
            }
            saveButton.onMouseClicked = {
                if (!pokaniApplication.gameScene.isOnlineGame) {
                    pokaniApplication.rootService.pokaniService.saveGame()
                }
                showSaveDialog()
            }

        }
    }

    /**
     * function that changes the Inputs of the [GameScene] and [ScoreboardScene] when the game Ended has been called
     * to show the [GameOverMenu] when pressing Escape instead of the [GameScenePauseMenu]
     */
    private fun changeEndGameEvent() {
        pokaniApplication.gameScene.onKeyPressed = {
            if (it.keyCode == KeyCode.TAB) {
                pokaniApplication.showGameScene(pokaniApplication.scoreboardScene)
            }
            if (it.keyCode == KeyCode.ESCAPE) {
                pokaniApplication.showMenuScene(pokaniApplication.gameOverMenu)
            }

        }
        pokaniApplication.scoreboardScene.onKeyPressed = {
            if (it.keyCode == KeyCode.TAB) {
                pokaniApplication.showGameScene(pokaniApplication.gameScene)
                pokaniApplication.gameSceneContainerUiUpdater.centerCameras()
            }
            if (it.keyCode == KeyCode.ESCAPE) {
                pokaniApplication.showMenuScene(pokaniApplication.gameOverMenu)
            }
        }
    }

    /**
     * Function which refreshes the [GameScene] and [ScoreboardScene] then a game start.
     *  Creating all relevant UI components.
     */
    override fun refreshOnStartGame() {
        pokaniApplication.gameScene.clearUIComponents()
        pokaniApplication.gameScene.createUIComponents()
        pokaniApplication.gameScene.undoButton.isVisible = false
        pokaniApplication.gameScene.undoButton.isDisabled = true
        pokaniApplication.gameScene.redoButton.isVisible = false
        pokaniApplication.gameScene.redoButton.isDisabled = true
        pokaniApplication.gameScene.undoButton.onMouseClicked = {
            pokaniApplication.rootService.pokaniService.undoMove()
        }
        pokaniApplication.gameScene.redoButton.onMouseClicked = {
            pokaniApplication.rootService.pokaniService.redoMove()
        }
        val currentGame = pokaniApplication.rootService.game
        val currentPlayer = currentGame.currentPlayer
        val currentMoveID = currentGame.moveID
        pokaniApplication.gameScene.skipButton.onMouseClicked = {
            pokaniApplication.rootService.pokaniService.executeMove(SkipMove(currentMoveID, currentPlayer))
        }
        pokaniApplication.gameSceneUIUpdater.updateUiAndContainers()
        pokaniApplication.gameScenePauseMenu.simDelayLabel.text = "SimDelay: ${pokaniApplication.gameScene.simDelay}ms"
        addShowGameSceneEvent()
        addShowScoreboardSceneEvent()
        addShowGameScenePauseMenuEvent()
        pokaniApplication.gameScene.simDelay = 1000
        if (pokaniApplication.gameScene.isOnlineGame) {
            pokaniApplication.rootService.game.players.forEachIndexed { index, player ->
                if (player.playerType == PlayerType.LOCAL_PLAYER) {
                    pokaniApplication.gameSceneUIUpdater.showPlayerCam(index)
                }
            }
        }
        pokaniApplication.gameSceneAnimator.startAnimiation()
        pokaniApplication.gameScene.playAnimation(
            DelayAnimation(1000).apply {
                onFinished = {
                    doBotMove()
                }
            }
        )
        pokaniApplication.gameScenePauseMenu.simDelayLabel.text = "SimDelay: ${pokaniApplication.gameScene.simDelay}ms"
    }

    /**
     * Function which refreshes the [GameScene] and [ScoreboardScene] after the game ends.
     */
    override fun refreshOnEndGame() {
        changeEndGameEvent()
        pokaniApplication.gameSceneUIUpdater.updateUiAndContainers()
        pokaniApplication.gameSceneUIUpdater.disableSelections()
        pokaniApplication.gameScene.undoButton.isVisible = false
        pokaniApplication.gameScene.redoButton.isVisible = false
        pokaniApplication.gameSceneAnimator.stopAnimation()
    }

    /**
     * Function which refreshes the [GameScene] and [ScoreboardScene] after a redo.
     */
    override fun refreshOnRedo(executeMovePlayer: Player) {
        pokaniApplication.gameSceneUIUpdater.updateUiAndContainers()
        doBotMove()
    }

    override fun refreshPreMove(
        sendMoveOnline: Boolean,
        move: AbstractMove,
        executeMovePlayer: Player,
        isSimulation: Boolean
    ) {
        if (executeMovePlayer.playerType == PlayerType.SMART_BOT || executeMovePlayer.playerType == PlayerType.RANDOM_BOT) {
            pokaniApplication.gameScene.playAnimation(
                SequentialAnimation(
                    DelayAnimation(10).apply {
                        onFinished = {
                            pokaniApplication.gameSceneContainerUiUpdater.updateGrids()
                            pokaniApplication.gameSceneContainerUiUpdater.updateDisplayLayout()
                            pokaniApplication.gameSceneContainerUiUpdater.updateIntermezzo()
                            pokaniApplication.gameSceneContainerUiUpdater.updateDisplayLayout()
                        }
                    },
                    DelayAnimation(pokaniApplication.gameScene.simDelay).apply {
                        onFinished = {
                            pokaniApplication.rootService.pokaniService.executeSendMove(
                                sendMoveOnline,
                                move,
                                executeMovePlayer,
                                isSimulation
                            )
                        }
                    },
                )
            )
        }else {
            pokaniApplication.rootService.pokaniService.executeSendMove(
                sendMoveOnline,
                move,
                executeMovePlayer,
                isSimulation
            )
        }
    }

    private fun doBotMove() {
        when (pokaniApplication.rootService.game.currentPlayer.playerType) {
            PlayerType.RANDOM_BOT -> {
                pokaniApplication.rootService.aiService.executeBotMove(PlayerType.RANDOM_BOT)
            }

            PlayerType.SMART_BOT -> {
                pokaniApplication.rootService.aiService.executeBotMove(PlayerType.SMART_BOT)
            }

            else -> return
        }
    }

    /**
     * Function which refreshes the [GameScene] and [ScoreboardScene] after an undo.
     */
    override fun refreshOnUndo() {
        pokaniApplication.gameSceneUIUpdater.updateUiAndContainers()
    }

    override fun refreshOnError() {
        val errorAnimation = DelayAnimation(2000).apply {
            pokaniApplication.gameScene.errorLabel.isVisible = true
            onFinished = {
                pokaniApplication.gameScene.errorLabel.isVisible = false
            }
        }
        pokaniApplication.gameScene.playAnimation(errorAnimation)
    }

}